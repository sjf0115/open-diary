package com.sjf.open

/**
  * Created by xiaosi on 16-8-9.
  */
object Test {
  def ifTest(args : Array[String]): Unit ={
    val arg = if(!args.isEmpty) args(0) else "default.txt"
    println(arg)
  }
  def main(args: Array[String]): Unit ={
    val array  = new Array[String](0)
    //array(0) = "a.txt"
    ifTest(array)
  }
}
