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
import cl.ucn.disc.dsm.manews.services.Transformer;
import net.openhft.hashing.LongHashFunction;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.threeten.bp.ZonedDateTime;


/**
 * Bugs Not Fixed
 */


public class NwTransformer {

  /**
   * The Logger
   */
  private static final Logger log = LoggerFactory.getLogger(NwTransformer.class);

  @RequiresApi(api = VERSION_CODES.O)
  public static Noticia transform(NwResult result) {

    //initial validations
    if (result == null) {
      throw new NewsapiException("Result is NULL");
    } else if (result.displayTitle == null) {
      throw new NewsapiException("WebURL is NULL");
    }


    if (result.displayTitle == null || result.publicationDate == null) {
      throw new NewsapiException("Title or date NULL");
    }
    //try to format date
    ZonedDateTime publishedAt;
    try {
      publishedAt = Transformer.parseZonedDateTime(result.publicationDate)
          .withZoneSameInstant(Noticia.ZONE_ID);
    } catch (Exception e) {
      log.error("No es posible tranformar el webPublicationDate con threetenabp.");
      throw new NewsapiException("Error in format time");
    }

    String resumen = "";
    //validate fields and define default
    if (result.link == null) {
      result.link = new Link();
      result.link.url = null;
      result.link.url = "Content not found";
      resumen = "Content not found";

    } else {
      //delete html code
      resumen = result.link.url;
      if (resumen.contains("<p>")) {
        resumen = StringUtils.substringBetween(resumen,
            "<p>", "</p>");

      } else if (resumen.contains("<li>")) {
        int pos1 = resumen.indexOf("<li>");
        int pos2 = resumen.indexOf("</li>");
        resumen = resumen.substring(pos1 + 4, pos2);
      }

      if (resumen.contains("<strong>")) {
        resumen = resumen.replace("<strong>", "");
        resumen = resumen.replace("</strong>", "");
      }

      if (resumen.contains("<br>")) {
        resumen = resumen.replace("<br>", "");
      }
      if (resumen.contains("<hr>")) {
        resumen = resumen.replace("<hr>", "");
      }
    }
    // The unique id (computed from hash)
    final Long theId = LongHashFunction.xx()
        .hashChars(result.displayTitle + result.publicationDate);

    return new Noticia(
        theId,
        result.displayTitle, "New York Times",
        "New York Times",
        result.displayTitle,
        result.link.type, resumen, result.link.url, publishedAt);
  }
}

