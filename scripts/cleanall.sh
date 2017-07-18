#!/bin/bash

./sbt -J-Xmx2G "project accumulo" clean  || { exit 1; }
./sbt -J-Xmx2G "project cassandra" clean  || { exit 1; }
./sbt -J-Xmx2G "project geomesa" clean || { exit 1; }
./sbt -J-Xmx2G "project geotools" clean || { exit 1; }
./sbt -J-Xmx2G "project geowave" clean || { exit 1; }
./sbt -J-Xmx2G "project hbase" clean || { exit 1; }
./sbt -J-Xmx2G "project proj4" clean || { exit 1; }
./sbt -J-Xmx2G "project s3" clean || { exit 1; }
./sbt -J-Xmx2G "project s3-test" clean || { exit 1; }
./sbt -J-Xmx2G "project raster-test" clean || { exit 1; }
./sbt -J-Xmx2G "project shapefile" clean || { exit 1; }
./sbt -J-Xmx2G "project slick" clean || { exit 1; }
./sbt -J-Xmx2G "project spark" clean  || { exit 1; }
./sbt -J-Xmx2G "project util" clean || { exit 1; }
./sbt -J-Xmx2G "project vector-test" clean || { exit 1; }
./sbt -J-Xmx2G "project vectortile" clean || { exit 1; }

rm -r accumulo/target
rm -r cassandra/target
rm -r geomesa/target
rm -r geotools/target
rm -r geowave/target
rm -r hbase/target
rm -r macros/target
rm -r proj4/target
rm -r raster-test/target
rm -r raster-testkit/target
rm -r raster/target
rm -r s3/target
rm -r s3-test/target
rm -r s3-testkit/target
rm -r shapefile/target
rm -r slick/target
rm -r spark-testkit/target
rm -r spark/target
rm -r util/target
rm -r vector-test/target
rm -r vector-testkit/target
rm -r vector/target
rm -r vectortile/target
