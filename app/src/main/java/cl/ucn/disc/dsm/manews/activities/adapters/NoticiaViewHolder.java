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
package cl.ucn.disc.dsm.manews.activities.adapters;

import androidx.recyclerview.widget.RecyclerView;
import cl.ucn.disc.dsm.manews.databinding.RowNoticiaBinding;
import cl.ucn.disc.dsm.manews.model.Noticia;

/**
 * ViewHolder Pattern.
 *
 * @author Martin Osorio Bugueño.
 */
public final class NoticiaViewHolder extends RecyclerView.ViewHolder {

  /**
   * The Bindings
   */
  private final RowNoticiaBinding binding;

  /**
   * The Constructor.
   *
   * @param rowNoticiaBinding to use.
   */
  public NoticiaViewHolder(RowNoticiaBinding rowNoticiaBinding) {
    super(rowNoticiaBinding.getRoot());
    this.binding = rowNoticiaBinding;
  }

  /**
   * Bind the Noticia to the ViewHolder.
   *
   * @param noticia to bind.
   */
  public void bind(final Noticia noticia) {

    this.binding.tvTitulo.setText(noticia.getTitulo());
    this.binding.tvResumen.setText(noticia.getResumen());
    this.binding.tvAutor.setText(noticia.getAutor());
    this.binding.tvFuente.setText(noticia.getFuente());

    // FIXME: The format of the date.
    this.binding.tvFecha.setText(noticia.getFecha().toString());

  }

}

