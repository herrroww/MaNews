/* Copyright [2020] [Martin Osorio]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cl.ucn.disc.dsm.manews.activities;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import cl.ucn.disc.dsm.manews.activities.adapters.NoticiaAdapter;
import cl.ucn.disc.dsm.manews.databinding.ActivityMainBinding;
import cl.ucn.disc.dsm.manews.model.Noticia;
import cl.ucn.disc.dsm.manews.services.newsapi.NewsApiNoticiaService;
import cl.ucn.disc.dsm.manews.services.NoticiaService;
import java.util.List;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <Martin Osorio Bugueño>
 */
public class MainActivity extends AppCompatActivity {

  /**
   * The Logger
   */
  private static final Logger log = LoggerFactory.getLogger(MainActivity.class);

  /**
   * The Adapter
   */
  private NoticiaAdapter noticiaAdapter;

  /**
   * The NoticiaService
   */
  private NoticiaService noticiaService;

  /**
   * @param savedInstanceState to use.
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Inflate the layout
    ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());

    // Assign to the main view.
    setContentView(binding.getRoot());

    // Set the toolbar
    {
      this.setSupportActionBar(binding.toolbar);
    }

    // The refresh
    {
      binding.swlRefresh.setOnRefreshListener(() -> {
        log.debug("Refreshing ..");
      });
    }

    // The Adapter + RecyclerView
    {
      // The Adapter
      this.noticiaAdapter = new NoticiaAdapter();

      // The Adapter
      binding.rvNoticias.setAdapter(this.noticiaAdapter);

      // The layout (ListView)
      binding.rvNoticias.setLayoutManager(new LinearLayoutManager(this));

      // The separator (line)
      binding.rvNoticias.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    // The NoticiaService
    this.noticiaService = new NewsApiNoticiaService();

    // The refresh
    {
      binding.swlRefresh.setOnRefreshListener(() -> {
        log.debug("Refreshing ..");

        // Execute in background ..
        AsyncTask.execute(() -> {

          // How much time do we need?
          final StopWatch stopWatch = StopWatch.createStarted();

          try {

            // 1. Get the List from NewsApi (in background)
            final List<Noticia> noticias = this.noticiaService.getNoticias(50);

            // (in UI)
            this.runOnUiThread(() -> {

              // 2. Set in the adapter (
              this.noticiaAdapter.setNoticias(noticias);

              // 3. Show a Toast!
              Toast.makeText(this, "Done: " + stopWatch, Toast.LENGTH_SHORT).show();

            });

          } catch (Exception ex) {

            log.error("Error", ex);

            // (in UI)
            this.runOnUiThread(() -> {

              // Build the message
              final StringBuffer sb = new StringBuffer("Error: ");
              sb.append(ex.getMessage());
              if (ex.getCause() != null) {
                sb.append(", ");
                sb.append(ex.getCause().getMessage());
              }

              // 3. Show the Toast!
              Toast.makeText(this, sb.toString(), Toast.LENGTH_LONG).show();

            });

          } finally {

            // 4. Hide the spinning circle
            binding.swlRefresh.setRefreshing(false);

          }

        });

      });
    }

  }

}