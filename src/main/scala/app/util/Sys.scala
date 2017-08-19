package app.util

/**
 * Windowsでjarファイルをダブルクリックで開いた場合、プログラムが終了すると同時にウィンドウが閉じられてしまう。
 * よって、エラーメッセージを出力してシステム終了するとエラーメッセージが見えないままウィンドウが閉じてしまう。
 * このobjectはそれを避けるためのモジュール。
 */
object Sys {
  /**
   * `message`を標準エラー出力に出力する。
   * その後エンターを入力するとプログラムが終了する。
   */
  def exit(message: String): Nothing = {
    Console.err.println(message)
    Console.err.println("エンターを入力すると終了します。")
    io.StdIn.readLine()
    sys.exit(1)
  }
}
