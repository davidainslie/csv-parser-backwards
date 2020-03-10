package com.backwards

import shapeless.tag.@@

package object tag {
  implicit class TagOps[A](val a: A) extends AnyVal {
    def tag[T]: A @@ T = a.asInstanceOf[A @@ T]
  }
}