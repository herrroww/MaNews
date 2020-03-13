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

import android.os.Build.VERSION_CODES;
import androidx.annotation.RequiresApi;
import cl.ucn.disc.dsm.manews.model.Noticia;
import cl.ucn.disc.dsm.manews.services.newsapi.NewsApiNoticiaService.NewsapiException;
import cl.ucn.disc.dsm.manews.services.newyorktimes.NwTransformer;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import retrofit2.Call;
import retrofit2.HttpException;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


class NewYorkTimesApiNoticeService {

  private final NewYorkTimesApi newYorkTimesApi;

  public NewYorkTimesApiNoticeService() {
    // https://futurestud.io/tutorials/retrofit-getting-started-and-android-client
    this.newYorkTimesApi = new Retrofit.Builder()
        // The main URL
        .baseUrl(NewYorkTimesApi.BASE_URL)
        // JSON to POJO
        .addConverterFactory(GsonConverterFactory.create())
        // Validate the interface
        .validateEagerly(true)
        // Build the Retrofit ..
        .build()
        // .. get the Content.
        .create(NewYorkTimesApi.class);

  }

  @RequiresApi(api = VERSION_CODES.N)
  public List<Noticia> getNoticiasFromCall(Call<NewYorkTimesApiResults> theCall) {

    try {
      // Get the result from the call
      final Response<NewYorkTimesApiResults> response = theCall.execute();

      // UnSuccessful !
      if (!response.isSuccessful()) {

        // Error!
        throw new TheNewYorkApiException(
            "Can't get the NewsResult, code: " + response.code(),
            new HttpException(response)
        );

      }

      final NewYorkTimesApiResults theResult = response.body();

      // No body
      if (theResult == null) {
        throw new TheNewYorkApiException("ContentResult was null");
      }

      // No articles
      if (theResult.response == null) {
        throw new TheNewYorkApiException("Content in NewsResult was null");
      }

      // Article to Noticia (transformer)
      return theResult.response.results.stream().map(NwTransformer::transform)
          .collect(Collectors.toList());

    } catch (final IOException ex) {
      throw new TheNewYorkApiException("Can't get the NewsResult", ex);
    }

  }

  public NewYorkTimesApi getNewYorkTimesApi() {
    return this.newYorkTimesApi;
  }

  public static final class TheNewYorkApiException extends RuntimeException {

    public TheNewYorkApiException(final String message) {
      super(message);
    }

    public TheNewYorkApiException(final String message, final Throwable cause) {
      super(message, cause);
    }
  }

}
