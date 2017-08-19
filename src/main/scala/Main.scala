import app.util.Sys
import com.github.tototoshi.csv.{ CSVReader, CSVWriter }
import org.jsoup.Connection.Method
import org.jsoup.{ Connection, Jsoup }

import scala.collection.JavaConverters._
import scala.util.Try

object Main extends App {
  val file = Try(CSVReader.open("input.csv")).getOrElse(Sys.exit("同じディレクトリにinput.csvをおいて再実行してください。"))
  implicit val credentials: java.util.Map[String, String] = getCredentials()

  val entries = getEntries

  println("hoge")

  // ----------- functions -----------
  /**
   * ログインに必要なcookieを取得する。
   */
  def getCredentials(): java.util.Map[String, String] = {
    val res = Jsoup.connect("http://freelancestylist.airsalon.net/wp-login.php")
      .data(Map("log" -> "airsalon", "pwd" -> "ldV8)nh(&)$TpsTK@kTWgu&Q").asJava)
      .method(Method.POST)
      .execute()
    res.cookies()
  }

  def getEntries(implicit credentials: java.util.Map[String, String]): String = {

    val form = Map(
      "_wpnonce" -> getNonce,
      "_wp_http_referer" -> "/wp-admin/tools.php?page=wp-csv-exporter",
      "post_id" -> "post_id",
      "type" -> "post",
      "post_status[]" -> "publish",
      "taxonomies[]" -> "category",
      "limit" -> "1000",
      "offset" -> "0",
      "order_by" -> "DESC",
      "string_code" -> "UTF-8",
      "post_date_from" -> "",
      "post_date_to" -> "",
      "post_modified_from" -> "",
      "post_modified_to" -> ""
    )
    // todo ここでエラーになる。Jsoup以外のクライアントでダウンロードする必要がありそう。
    val res = Jsoup.connect("http://freelancestylist.airsalon.net/wp-content/plugins/wp-csv-exporter/admin/download.php")
      .cookies(credentials)
      .data(form.asJava)
      .method(Method.POST)
      .execute()
    res.body
  }

  def getNonce(implicit credentials: java.util.Map[String, String]): String = {
    val doc = Jsoup.connect("http://freelancestylist.airsalon.net/wp-admin/tools.php?page=wp-csv-exporter")
      .cookies(credentials)
      .get()
    doc.getElementById("_wpnonce").`val`
  }
}
