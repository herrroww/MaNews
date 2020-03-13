/*
 *  Copyright [2020] [Martin Osorio Bugueño]
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */


package cl.ucn.disc.dsm.manews.services.newyorktimes;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface NewYorkTimesApi {

  /**
   * The URL
   */
  String BASE_URL = "https://api.nytimes.com/svc/archive/v1";

  /**
   * The API Key
   */
  String API_KEY = "G3WO5pBc9uApkNfsmz13ub8dNGAoTO5Y";


  @GET("search")
  Call<Response> getContent(
      @Query("api-key") String API_KEY,
      @Query("page-size") int page_size,
      @Query("show-fields") String fields);
}