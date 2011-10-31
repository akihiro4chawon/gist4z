package gist4z

object Gist4z extends api.Gists {
  implicit val defaultNullAuthSupply = auth.NullAuth
}