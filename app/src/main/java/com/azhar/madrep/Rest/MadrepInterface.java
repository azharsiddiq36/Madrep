package com.azhar.madrep.Rest;

import com.azhar.madrep.Model.ResponseAbsensi;
import com.azhar.madrep.Model.ResponseChangeFotoProfil;
import com.azhar.madrep.Model.ResponseDokter;
import com.azhar.madrep.Model.ResponseKategori;
import com.azhar.madrep.Model.ResponseKunjungan;
import com.azhar.madrep.Model.ResponseLogin;
import com.azhar.madrep.Model.ResponseObat;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface MadrepInterface {
    @FormUrlEncoded
    @POST("ApiPengguna/login")
    Call<ResponseLogin> loginRequest(@Field("pengguna_username") String nip,
                                     @Field("pengguna_password") String password);
    @GET("ApiDokter")
    Call<ResponseDokter> getListDokter();
    @GET("ApiObat")
    Call<ResponseObat> getListObat();
    @GET("ApiKunjungan/getAllKunjungan")
    Call<ResponseKunjungan> getListAllKunjungan();
    @FormUrlEncoded
    @POST("ApiKunjungan")
    Call<ResponseKunjungan> getListKunjungan(@Field("kunjungan_id_madrep") String id);
    @FormUrlEncoded
    @POST("ApiKunjungan/getByDokter")
    Call<ResponseKunjungan> getListKunjunganDokter(@Field("kunjungan_id_dokter") String id);
    @FormUrlEncoded
    @POST("ApiKunjungan/post")
    Call<ResponseKunjungan> addListKunjungan(@Field("kunjungan_id_madrep") String id,
                                             @Field("kunjungan_id_dokter") String id_dokter,
                                             @Field("kunjungan_cabang") String cabang,
                                             @Field("kunjungan_latitude") String latitude,
                                             @Field("kunjungan_longitude") String longitude,
                                             @Field("kunjungan_keterangan") String keterangan,
                                             @Field("kunjungan_obat") String kunjungan_obat);
    @FormUrlEncoded
    @POST("ApiPengguna/edit")
    Call<ResponseLogin> ubahPassword(@Field("pengguna_id") String id,
                                     @Field("pengguna_password") String pw);
    @GET("ApiKategori")
    Call<ResponseKategori> getListKategori();
    @Multipart
    @POST("ApiObat/post")
    Call<ResponseObat> uploadObat(@Part MultipartBody.Part image,
                                  @Part("obat_nama") RequestBody obat_nama,
                                  @Part("obat_kategori") RequestBody kategori,
                                  @Part("obat_rincian") RequestBody rincian);
    @Multipart
    @POST("ApiPengguna/changeFoto")
    Call<ResponseChangeFotoProfil> changeFoto(@Part MultipartBody.Part image,
                                              @Part("pengguna_id") RequestBody pengguna_id);
    @FormUrlEncoded
    @POST("ApiAbsensi/post")
    Call<ResponseAbsensi> addAbsensi(@Field("absensi_id_pengguna") String id,
                                     @Field("absensi_kota") String kota,
                                     @Field("absensi_kecamatan") String kecamatan,
                                     @Field("absensi_jalan") String jalan,
                                     @Field("absensi_kelurahan") String kelurahan,
                                     @Field("absensi_latitude") String latitude,
                                     @Field("absensi_longitude") String longitude);
    @FormUrlEncoded
    @POST("ApiAbsensi/getAbsen")
    Call<ResponseAbsensi> checkAbsen(@Field("absensi_id_pengguna") String id);
    @FormUrlEncoded
    @POST("ApiKunjungan/getTotal")
    Call<ResponseKunjungan> getTotalKunjungan(@Field("kunjungan_id_madrep") String id,
                                                @Field("kunjungan_hak_akses") String hak_akses);

    @GET("ApiKunjungan/getSemua")
    Call<ResponseKunjungan> getSemuaKunjungan();
    @GET("ApiObat/getTotal")
    Call<ResponseObat> getTotalObat();
    @GET("ApiDokter/getTotal")
    Call<ResponseDokter> getTotalDokter();
}
