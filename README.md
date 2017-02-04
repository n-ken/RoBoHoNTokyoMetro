# RoBoHoNTokyoMetro

[東京メトロAPI](https://developer.tokyometroapp.jp/info)を利用して、RoBoHoNと対話を行いながら当該API機能を利用するアプリケーションになります。

## 使い方

### アクセストークンの準備

``app/src/main/assets/``に``access_token.txt``を作成し、取得したアクセストークンを記録してください。

### アプリケーションの起動

以下のいずれかの方法でアプリケーションが起動します。

- ホーム画面でRoBoHoNに「東京メトロについて教えて」と発話。
- 背面LCDからアプリケーションを起動。

### 機能

現時点では、「運賃検索」および「東京メトロの各路線の運行情報確認」機能のみを実装しています。

基本的には、アプリケーション起動後、RoBoHoNと対話をしながら利用してください。

各種シナリオ起動中に、割り込みで機能を切り替えるようなシナリオ対応は行っておりません。
ただし、対話終了後に「運賃検索の依頼」または「運行情報の確認依頼」を発話すると、それぞれの機能が起動します。

> 例:   
> (運行情報確認後) ユーザー発話「次は運賃を調べて」
> RoBoHoN発話「はーい、運賃検索を開始するね」(運賃検索機能が起動)