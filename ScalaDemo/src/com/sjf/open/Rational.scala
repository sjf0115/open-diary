package com.sjf.open

/**
  * Created by xiaosi on 16-8-9.
  */
class Rational(a:Int, b:Int){
  require(b != 0, "分母不能为0")

  val number:Int = a
  val denom:Int = b

  // 最大公约数
  private val g = gcd(a.abs, b.abs)

  def this(a : Int) = this(a, 1)
  private def gcd(a:Int, b:Int): Int = if(b == 0) a else gcd(b, a % b)

  println("Rational : " + number + "/" + denom)

  override def toString = "value -> " + number + "/" + denom

  def add(r : Rational): Rational = new Rational(number * r.denom + denom * r.number, denom * r.denom)
  def +(r : Rational) : Rational = new Rational(number * r.denom + denom * r.number, denom * r.denom)
  def * (r : Rational) : Rational = new Rational(number * r.number , denom * r.denom)
}
