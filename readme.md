openid4java の確認用プロジェクト
================================

openid4java とは
----------------

openid4java とは、[code.google.com](https://code.google.com/p/openid4java/) でソースコードが公開されている Open ID を扱う Java ライブラリ。このプロジェクトは openid4java の使い方の備忘録。


セットアップ
------------

  Eclipse Java EE Developers、Apache Tomcat 7 で開発を行う。
  openid4java は 0.9.8 を使用した。
  Tomcat 7 を使用するのは、Eclipse Java EE Developers が Tomcat 8 に対応していないため。

  Eclipse と Tomcat は連動可能なように設定しておく。

    * メニューから [Window] → [Preferences] を選択
    * Preferences ダイアログの [Server] → [Runtime Environment] を選択
    * [Add] ボタンをクリック
    * Apache Tomcat v7.0 を選択
    * [Next] をクリック
    * Tomcat installation directory: に Tomcat を展開したフォルダを入力
    * [OK] をクリック

  こんな感じで設定できるはず。

  OS は Windows や Linux で動作確認がとれているが、もし Eclipse にインポートしてエラーが出るようなら、おそらくクラスパス関連なので、冷静に変更すること。
  

動作確認
--------

  Eclipse にインポートした OpenIDTest プロジェクトを Eclipse 上で Tomcat を使用して動かす。  
  Eclipse 内にブラウザが起動するので、そこで動作確認が行える。


プログラムの補足
----------------

* index.jsp

  いくつかの Open ID プロバイダが羅列されている。
  ここに羅列されているプロバイダは、私がアカウントを保持しているサービスでかつ、アカウント毎に固有 URL を要求しないもの。
  たとえば「はてな」の認証用 URL は、http://www.hatena.ne.jp/<account> のように、自身のシステムがアカウント名を知らなければならないようになっており、使い勝手（設計）が悪いため、本プロジェクトでは確認していない。

* result.jsp

  Open ID プロバイダからのレスポンスを表示する。
  プロバイダのサービスを利用する場合は、openid.identify というパラメータの値を使用するのだと思う。

* OpenID

  サーブレットの実装。
  このサーブレットは /login と /result というふたつの URI にマッピングされているが、それは ConsumerManager のインスタンスを複数作成しなくても良いようにするため。
  その理由は、参考と謝辞のサイトの更に先で議論されている。
  すべてのパスを同一のサーブレットでさばくのは、大規模になると無理が生じるため、他の方法を検討する必要があるだろう。

  openid4java を使用した他の実装も見てみたが、やるべきことはほぼ同じで、このサーブレットはコンパクトにまとまっている。

* lib

  JAR ファイルがたくさん入っているが、それらはすべて openid4java が要求するライブラリ。


参考と謝辞
----------

* [プログラマとSEのあいだ](http://d.hatena.ne.jp/taka_2/20110620/p2)
    * 実装の骨格はこのサイトから頂きました。
