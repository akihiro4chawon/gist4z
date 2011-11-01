package gist4z

object Gist4z {
  implicit val defaultNullAuthSupply = auth.NullAuth
  object Gists extends api.Gists
  object UserEmails extends api.UserEmails
}