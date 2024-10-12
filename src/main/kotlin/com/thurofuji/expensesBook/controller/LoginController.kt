package com.thurofuji.expensesBook.controller

import com.thurofuji.expensesBook.bean.LoginResponse
import com.thurofuji.expensesBook.bean.LoginRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class LoginController {

    // TODO 現在の構成だとフロントエンド（`localhost:3000`）とサーバーサイド（`localhost:8080`）が
    //  CrossOriginになってしまうのでアノテーションを付与している。将来的には構成を見直して不要とする予定
    @CrossOrigin(origins = ["http://localhost:3000"])
    @PostMapping("/api/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<LoginResponse> {
        // TODO ひとまず`ok`と`Bad Request`を分けるための最低限の実装のみ書いている
        //  ユーザーマスターなどへアクセスし、正しいユーザーであること、適切な権限を持っていることの確認を行う

        return if (request.username == "admin") {
            // TODO 決め打ちではなく、JWTを生成できるようにする
            val jwt = LoginResponse("eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxIiwiaXNzdWVyIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgwIiwic2NvcGUiOlsiZXhwZW5zZTpyZWFkIiwiZXhwZW5zZTp3cml0ZSJdLCJpYXQiOjE3MjU0MjU1NTgsImV4cCI6MTczNTY1NzE5OX0.lwhFgN9LAXDG2iU3zCQtPfCRpbRNraK58UK0e4aZQ7wv68-0oaiLbX3Q2hJeo7rUDfKGCXPNXSz0_sKQLFo6cjqp4scB6E5aVI43O98kE8UwBVKNGS3uH0iz8pW__MyELe7mdfDLJAONgES1cuGJs6Uh9nXh2rUo8kC915BrT8dRqSwA0zqkDa9j8A05EejDWYFg_YWdalYUcLHrMnFzzSWt6x3JQoXAhnDkW0CEInoXhZpxnJkZ4IF6of20ec1MuHfOU0flusKQ5vlkvRsY9E3ltEyCMs6gxjDUEcEJW9bbziv2ljvcuPmMisiFvIxtvGqHN18JB7jJYMyzDvVuNA")

            ResponseEntity.ok(jwt)
        } else {
            // TODO エラー内容に応じてレスポンスを変える、エラーメッセージを設定するなどの検討
            ResponseEntity.badRequest().build()
        }
    }

}
