# 概要

家計簿を管理するためのアプリケーション

## Front-End

// TODO フロントエンドについて決まったら書いていく

## Server-Side

### REST API

家計簿に対して出費の追加/更新/削除を行ったり、一覧の取得を行ったりするREST APIを提供する

/api/expenseBook

// TODO まだ暫定的なものなので、実装を進めながら内容を見直す

#### GET

家計簿の取得

##### 年月の指定

###### request /list/{yyyyMM}

指定された年月の家計一覧を取得

パラメータの指定なしであれば当月

###### response

| name    | description                                                                |
|---------|----------------------------------------------------------------------------|
| result  | 成功したらOK,失敗したらNG                                                            |
| error   | NGの場合に、エラー内容                                                               |
| list    | 合致する家計の一覧                                                                  |
| - date  | 購入した日付                                                                     |
| - price | 支払金額                                                                       |
| - store | 支払先                                                                        |
| - type  | 1: 食費・雑費（スーパーなど）、2: 食費・雑費（外食など）、3: 娯楽、4: 身嗜み、ガソリン、5: 設備、6: 学習、7: 医療、8: その他 |

##### 詳細の取得

###### request /detail/{id}

指定された出費1件の取得

###### response

| name   | description                                                                |
|--------|----------------------------------------------------------------------------|
| result | 成功したらOK,失敗したらNG                                                            |
| error  | NGの場合に、エラー内容                                                               |
| id     | 出費のid                                                                      |
| date   | 購入した日付                                                                     |
| price  | 支払金額                                                                       |
| store  | 支払先                                                                        |
| type   | 1: 食費・雑費（スーパーなど）、2: 食費・雑費（外食など）、3: 娯楽、4: 身嗜み、ガソリン、5: 設備、6: 学習、7: 医療、8: その他 |


#### POST

出費1件の新規登録

##### request

JSONで送信

| name  | required | default | description                                                                |
|-------|----------|---------|----------------------------------------------------------------------------|
| date  | X        | システム日   | 購入した日付を指定（yyyy/MM/dd）                                                      |
| price | O        | N/A     | 支払金額                                                                       |
| store | X        | 空文字     | 支払先                                                                        |
| type  | O        | N/A     | 1: 食費・雑費（スーパーなど）、2: 食費・雑費（外食など）、3: 娯楽、4: 身嗜み、ガソリン、5: 設備、6: 学習、7: 医療、8: その他 |

##### response

| name   | description       |
|--------|-------------------|
| result | 成功したらOK,失敗したらNG   |
| id     | OKの場合に、登録された家計のid |
| error  | NGの場合に、エラー内容      |

#### PUT

登録済みの出費1件を更新する

##### request /{id}

| name  | required | default | description                                                                |
|-------|----------|---------|----------------------------------------------------------------------------|
| date  | X        | システム日   | 購入した日付を指定（yyyy/MM/dd）                                                      |
| price | O        | N/A     | 支払金額                                                                       |
| store | X        | 空文字     | 支払先                                                                        |
| type  | O        | N/A     | 1: 食費・雑費（スーパーなど）、2: 食費・雑費（外食など）、3: 娯楽、4: 身嗜み、ガソリン、5: 設備、6: 学習、7: 医療、8: その他 |

##### response

| name   | description      |
|--------|------------------|
| result | 成功したらOK,失敗したらNG  |
| id     | OKの場合に、更新した出費のid |
| error  | NGの場合に、エラー内容     |

#### DELETE

登録済みの出費1件を削除する

##### request /{id}

指定された出費1件の削除

##### response

| name   | description      |
|--------|------------------|
| result | 成功したらOK,失敗したらNG  |
| id     | OKの場合に、削除した出費のid |
| error  | NGの場合に、エラー内容     |

### DB

#### 出費履歴

| name   | type      | PK | not null | default           | description      |
|--------|-----------|----|----------|-------------------|------------------|
| id     | uuid      | ◯  | ◯        | gen_random_uuid() | 出費を一意にするid       |
| 支払日    | date      |    | ◯        |                   | 支払った日付           |
| 費目cd   | integer   |    | ◯        |                   | 費目マスターの費目cd      |
| 金額     | integer   |    | ◯        |                   | 支払った金額。マイナスも許容する |
| 支払先    | text      |    | ◯        |                   | 支払先              |
| 最終更新日時 | timestamp |    | ◯        | CURRENT_TIMESTAMP | 出費を最後に更新した日時     |


#### 費目マスター
| name   | type      | PK | not null | default | description                 |
|--------|-----------|----|----------|---------|-----------------------------|
| 費目cd   | integer   | ◯  | ◯        |         | 費目を一意にするコード                 |
| 費目名    | text      |    | ◯        |         | 費目の名称                       |
| 有効区分   | boolean   |    | ◯        | true    | 費目の有効区分。無効の場合、新規登録で選択できなくなる |

[//]: # (TODO とりあえず最低限必要そうな情報だけで定義している。)
[//]: # (TODO できれば、複数人利用や不正利用対策などで、`登録者`)




