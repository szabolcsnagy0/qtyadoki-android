package hu.android.qtyadoki.api

import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.google.gson.GsonBuilder
import hu.android.qtyadoki.data.AppointmentData
import hu.android.qtyadoki.data.LoginData
import hu.android.qtyadoki.data.MedicationData
import hu.android.qtyadoki.data.NewAppointment
import hu.android.qtyadoki.data.NewPet
import hu.android.qtyadoki.data.OwnerModel
import hu.android.qtyadoki.data.OwnerRegistrationModel
import hu.android.qtyadoki.data.PetModel
import hu.android.qtyadoki.data.SelectableAppointment
import hu.android.qtyadoki.data.SelectedDate
import hu.android.qtyadoki.data.TokenData
import hu.android.qtyadoki.data.TransferData
import hu.android.qtyadoki.data.VetModel
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


/**
 * Retrofit interface for the API calls
 */
interface ApiService {
    /**
     * Transfer the given pet to the given owner
     * @body transferModel The data of the transfer
     */
    @POST("/pet/transfer")
    fun transferPet(@Body transferModel: TransferData): Call<ResponseBody>?

    /**
     * Accept the given pet
     * @param petId The id of the pet
     */
    @POST("/pet/accept/{id}")
    fun acceptPet(@Path("id") petId: Int): Call<ResponseBody>?

    /**
     * Reject the given pet
     * @param petId The id of the pet
     */
    @POST("/pet/reject/{id}")
    fun rejectPet(@Path("id") petId: Int): Call<ResponseBody>?

    /**
     * Test whether the token is valid
     */
    @GET("/user/loginwelcome")
    fun testToken(): Call<ResponseBody>?

    /**
     * Get the list of the free appointments of the given vet
     * @body date The date of the appointments
     */
    @POST("/appointment/free")
    fun getFreeAppointmentsOfVet(@Body date: SelectedDate): Call<List<SelectableAppointment>?>?

    /**
     * Add a new appointment
     * @body newAppointment The data of the new appointment
     */
    @POST("/appointment/add")
    fun addAppointment(@Body newAppointment: NewAppointment): Call<ResponseBody>?

    /**
     * Get the list of the vets
     */
    @GET("/user/all/vet")
    fun getVetList(): Call<List<VetModel>?>?

    /**
     * Get the list of the appointments of the user
     */
    @GET("/appointment/all/owner")
    fun getAppointmentsList(): Call<List<AppointmentData>?>?

    /**
     * Get the appointment data of the given appointment
     * @param appId The id of the appointment
     */
    @GET("/report/find/appId/{appId}")
    fun getAppointmentData(@Path("appId") appId: Int): Call<AppointmentData?>?

    /**
     * Get the list of the pets of the user
     * @param petId The id of the pet
     */
    @GET("/pet/find/owner/{petId}")
    fun getPetById(@Path("petId") petId: Int): Call<PetModel?>?

    /**
     * Get the list of the pets of the user
     */
    @GET("/pet/owner")
    fun getPetsList(): Call<List<PetModel>?>?

    /**
     * Add a new pet
     * @body newPet The data of the new pet
     */
    @POST("/pet/new")
    fun addPet(@Body newPet: NewPet?): Call<PetModel>?

    /**
     * Update the pet data
     * @body petModel The data of the pet
     */
    @PUT("/pet/update")
    fun updatePet(@Body petModel: PetModel?): Call<ResponseBody>?

    /**
     * Get the medications of the given pet
     * @param petId The id of the pet
     */
    @GET("/presc/find/pet/{petId}")
    fun getMedicationsOfPet(@Path("petId") petId: Int): Call<List<MedicationData>?>?

    /**
     * Get the user data
     */
    @GET("/user/profile")
    fun getUserData(): Call<OwnerModel?>?

    /**
     * Update the user data
     * @body ownerModel The data of the user
     */
    @PUT("/user/update")
    fun updateUserData(@Body ownerModel: OwnerModel?): Call<ResponseBody>?


    /**
     * Upload a new profile picture
     * @body image The image
     */
    @PUT("/user/picture")
    fun updateUserPicture(
        @Body image: RequestBody
    ): Call<ResponseBody>?

    /**
     * Upload a new pet picture
     * @param petId The id of the pet
     * @body image The image
     */
    @PUT("/pet/picture/{petId}")
    fun updatePetPicture(
        @Path("petId") petId: Int,
        @Body image: RequestBody
    ): Call<ResponseBody>?

    /**
     * Register a new user
     * @body ownerModel The data of the user
     */
    @POST("/authentication/register/owner")
    @Headers("No-Authentication: true")
    fun registerUser(@Body ownerModel: OwnerRegistrationModel?): Call<OwnerRegistrationModel?>?

    /**
     * Login the user
     * @body data The data of the user
     */
    @POST("/authentication/login/owner")
    @Headers("No-Authentication: true")
    fun loginUser(@Body data: LoginData?): Call<TokenData?>?

    companion object {
        var api: ApiService? = null
        var tokenManager: TokenManager? = null
        private const val BASE_URL = "http://qtyadokibackend.eu-north-1.elasticbeanstalk.com"

        /**
         * Get the singleton instance of the ApiService
         */
        fun getInstance(): ApiService {
            if (api == null) {
                val client = OkHttpClient.Builder()
                    .addInterceptor(ServiceInterceptor(tokenManager))
                    .build()
                val gson = GsonBuilder()
                    .setLenient()
                    .create()
                api = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build().create(ApiService::class.java)
            }
            return api!!
        }

        /**
         * Get the GlideUrl for the given endpoint
         */
        private fun getGlideURL(endPoint: String): GlideUrl? {
            if (tokenManager == null) return null
            val token = tokenManager!!.fetchAuthToken() ?: return null
            val url = "$BASE_URL$endPoint"

            return GlideUrl(
                url,
                LazyHeaders.Builder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
            )
        }

        /**
         * Get the GlideUrl for the profile picture
         */
        fun getGlideURLProfile(): GlideUrl? {
            return getGlideURL("/user/picture")
        }

        /**
         * Get the GlideUrl for the pet picture
         */
        fun getGlideURLPet(petId: Int?): GlideUrl? {
            if(petId == null) return null
            return getGlideURL("/pet/picture/$petId")
        }
    }
}