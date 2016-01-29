package geotrellis.raster.reproject

import geotrellis.raster._
import geotrellis.raster.resample._
import geotrellis.vector.Extent
import geotrellis.proj4._

import spire.syntax.cfor._

trait SingleBandRasterReprojectMethods extends RasterReprojectMethods[SingleBandRaster] {
  import Reproject.Options

  def reproject(
    targetRasterExtent: RasterExtent, 
    transform: Transform,
    inverseTransform: Transform,
    options: Options
  ): SingleBandRaster = {
    val Raster(tile, extent) = self
    val RasterExtent(_, cellwidth, cellheight, _, _) = self.rasterExtent
    val RasterExtent(newExtent, newCellWidth, newCellHeight, newCols, newRows) = targetRasterExtent

    val newTile = ArrayTile.empty(tile.cellType, newCols, newRows)

    val rowTransform: RowTransform =
      if (options.errorThreshold != 0.0)
        RowTransform.approximate(inverseTransform, options.errorThreshold)
      else
        RowTransform.exact(inverseTransform)

    // The map coordinates of the destination raster
    val (topLeftX, topLeftY) = targetRasterExtent.gridToMap(0,0)
    val destX = Array.ofDim[Double](newCols)
    var currX = topLeftX
    cfor(0)(_ < newCols, _ + 1) { i =>
      destX(i) = currX
      currX += newCellWidth
    }

    val destY = Array.ofDim[Double](newCols).fill(topLeftY)

    // The map coordinates of the source raster, transformed from the
    // destination map coordinates on each row iteration
    val srcX = Array.ofDim[Double](newCols)
    val srcY = Array.ofDim[Double](newCols)

    val resampler = Resample(options.method, tile, extent, CellSize(newCellWidth, newCellHeight))

    if(tile.cellType.isFloatingPoint) {
      val resample = resampler.resampleDouble _
      cfor(0)(_ < newRows, _ + 1) { row =>
        // Reproject this whole row.
        rowTransform(destX, destY, srcX, srcY)
        cfor(0)(_ < newCols, _ + 1) { col =>
          val v = resample(srcX(col), srcY(col))
          newTile.setDouble(col, row, v)

          // Add row height for next iteration
          destY(col) -= newCellHeight
        }
      }
    } else {
      val resample = resampler.resample _
      cfor(0)(_ < newRows, _ + 1) { row =>
        // Reproject this whole row.
        rowTransform(destX, destY, srcX, srcY)
        cfor(0)(_ < newCols, _ + 1) { col =>
          val x = srcX(col)
          val y = srcY(col)

          val v = resample(x, y)
          newTile.set(col, row, v)

          // Add row height for next iteration
          destY(col) -= newCellHeight
        }
      }
    }

    Raster(newTile, newExtent)
  }
}
