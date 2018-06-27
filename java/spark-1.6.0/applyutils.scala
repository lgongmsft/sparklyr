package sparklyr

import org.apache.spark._
import org.apache.spark.rdd.RDD
import org.apache.spark.sql._
import org.apache.spark.sql.types._
import org.apache.spark.sql.catalyst.encoders.RowEncoder

object ApplyUtils {
  def groupBy(rdd: RDD[Row], colPosition: Array[Int]): RDD[Row] = {
    rdd.groupBy(
      r => colPosition.map(p => r.get(p)).mkString("|")
    ).map(
      r => Row(r._2.toSeq)
    )
  }

  def groupBy(
    data: org.apache.spark.sql.Dataset[Row],
    colPosition: Array[Int]): org.apache.spark.sql.Dataset[Row] = {

    val schema: StructType = data.schema
    val encoder = RowEncoder(schema)

    data.groupBy(
      r => colPosition.map(p => r.get(p)).mkString("|")
    )(org.apache.spark.sql.Encoders.STRING).mapGroups(
      (k, r) => Row(r.toSeq)
    )(encoder)

  }
}
