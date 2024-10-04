package com.thurofuji.expensesBook.controller

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
    fun login(@RequestBody request: LoginRequest): ResponseEntity<Void> {
        // TODO ひとまず`ok`と`Bad Request`を分けるための最低限の実装のみ書いている
        //  ユーザーマスターなどへアクセスし、正しいユーザーであること、適切な権限を持っていることの確認を行う

        // TODO ログインに成功したらJWTのトークンを生成してフロントに返す

        return if (request.username == "admin") {
            ResponseEntity.ok().build()
        } else {
            ResponseEntity.badRequest().build()
        }
    }

}
