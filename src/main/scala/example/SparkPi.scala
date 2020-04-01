/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package example

import org.apache.spark.rdd.RDD

import scala.math.random
import org.apache.spark.{SparkConf, SparkContext}

/** Computes an approximation to pi */
object SparkPi {
  def main(args: Array[String]): Unit = {

    val sparkContext =
      SparkContext.getOrCreate(
        (new SparkConf(loadDefaults =  true))
          .setIfMissing("spark.master","local[*]")
          .setAppName("Demo"))


    val slices = 100
    val n = math.min(100000L * slices, Int.MaxValue).toInt // avoid overflow

    val count = sparkContext.parallelize(1 until n, slices).map { i =>
      val x = random * 2 - 1
      val y = random * 2 - 1
      if (x*x + y*y <= 1) 1 else 0
    }.reduce(_ + _)

    println(s"Pi is roughly ${4.0 * count / (n - 1)}")

    sparkContext.stop()
  }
}