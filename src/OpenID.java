import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.openid4java.consumer.ConsumerManager;
import org.openid4java.consumer.VerificationResult;
import org.openid4java.discovery.DiscoveryInformation;
import org.openid4java.discovery.Identifier;
import org.openid4java.message.AuthRequest;
import org.openid4java.message.ParameterList;

public class OpenID extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/** Open ID マネージャインスタンス。複数作らない方が良いらしい。 */
	public ConsumerManager manager;
	/** Open ID サービスからログイン後に戻ってくる URL。 */
	private static final String RETURN_URL = "http://localhost:8080/OpenIDTest/result";

	@Override
	public void init() throws ServletException {
		manager = new ConsumerManager();
	}

	/**
	 * post にしか対応しない Open ID サービスもあるようだ。
	 */
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse res)
		throws ServletException, IOException {
		doGet(req, res);
	}

	/**
	 * get にしか対応しない Open ID サービスもあるようだ。
	 */
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String uri = req.getRequestURI();
		if (uri.equals("/OpenIDTest/login")) {
			requestOpenId(req, res);
		} else if (uri.equals("/OpenIDTest/result")) {
			responseOpenId(req, res);
		}
	}

	private void requestOpenId(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		try {
			// どの Open ID プロバイダを使用するか外部からパラメータで受け取り
			String identifier = req.getParameter("identifier");
			// 指定された識別子を検出する
			@SuppressWarnings("rawtypes")
			List discoveries = manager.discover(identifier);

			// Open ID プロバイダとの関連付けを試み、認証のためのサービスエンドポイントをひとつ取得する
			DiscoveryInformation discovered = manager.associate(discoveries);

			// 後で使用するためにユーザーのセッションで発見情報を保存
			// セッションが存在しない場合の処理は省略
			HttpSession session = req.getSession(false);
			session.setAttribute("discovered", discovered);

			// Open ID プロバイダに送信する AuthRequest メッセージ取得
			AuthRequest authReq = manager.authenticate(discovered, RETURN_URL);

			// Open ID プロバイダにリダイレクト
			res.sendRedirect(authReq.getDestinationUrl(true));
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	private void responseOpenId(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		try {
			// 認証レスポンスからパラメータリストを作成
			ParameterList openidResp = new ParameterList(req.getParameterMap());

			// セッションに保持しておいた発見情報を取り出す
			HttpSession session = req.getSession(false);
			DiscoveryInformation discovered = (DiscoveryInformation) session.getAttribute("discovered");

			// HTTP リクエストから URL を生成
			StringBuffer receivingURL = req.getRequestURL();
			String queryString = req.getQueryString();
			if (queryString != null && !queryString.isEmpty()) {
				receivingURL.append("?").append(req.getQueryString());
			}

			// レスポンスを検証
			VerificationResult verification = manager.verify(new String(receivingURL), openidResp, discovered);

			
			// 検証結果から検証された識別子を取得
			Identifier verified = verification.getVerifiedId();

			if (verified != null) {
				// Open ID プロバイダから Open ID の取得に成功
				req.getRequestDispatcher("/result.jsp").forward(req, res);
			} else {
				// 失敗
				req.getRequestDispatcher("/index.jsp").forward(req, res);
			}
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}
}