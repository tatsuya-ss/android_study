// プラグインの設定
// android アプリケーションとして必要な基本プラグインを適用
plugins {
    id("com.android.application")  // Androidアプリケーションビルド用のプラグイン
    id("org.jetbrains.kotlin.android")  // KotlinのAndroidサポート用プラグイン
}

// Androidアプリケーションの設定
android {
    // アプリケーションの名前空間
    // これはアプリケーションの一意の識別子として使用される
    namespace = "com.example.android_study"

    // コンパイル時に使用するAndroid SDKのバージョン
    // 最新のAPI機能を使用可能にする
    compileSdk = 35

    // アプリケーションのデフォルト設定
    defaultConfig {
        // アプリケーションのパッケージ名（Google Play Storeでの一意の識別子）
        applicationId = "com.example.android_study"

        // サポートする最小のAndroid SDKバージョン（Android 7.0）
        minSdk = 24

        // アプリケーションのターゲットとなるAndroid SDKバージョン
        targetSdk = 35

        // アプリケーションのバージョン管理用の数値
        versionCode = 1

        // ユーザーに表示されるバージョン名
        versionName = "1.0"

        // インストルメンテーションテスト実行用の設定
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    // ビルドタイプの設定
    buildTypes {
        // リリースビルドの設定
        release {
            // コード最適化の設定（現在は無効）
            // 推奨: プロダクションリリース時には true に設定することを検討
            isMinifyEnabled = false

            // ProGuard設定ファイルの指定
            // コード難読化やサイズ最適化のルールを定義
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    // ビルド機能の設定
    buildFeatures {
        // ViewBindingを有効化
        // レイアウトファイルへのバインディングクラスを自動生成
        viewBinding = true
    }

    // Javaコンパイルオプションの設定
    compileOptions {
        // ソースコードとターゲットのJavaバージョンを指定
        // Java 11を使用
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    // Kotlinコンパイルオプションの設定
    kotlinOptions {
        // KotlinのJVMターゲットバージョンを指定
        jvmTarget = "11"
    }
}

// プロジェクトの依存関係を定義
dependencies {
    // AndroidX コア依存関係
    implementation(libs.androidx.core.ktx)  // Kotlinの拡張機能
    implementation(libs.androidx.appcompat)  // 下位互換性サポート
    implementation(libs.material)  // マテリアルデザインコンポーネント
    implementation(libs.androidx.activity)  // アクティビティの基本機能
    implementation(libs.androidx.constraintlayout)  // 制約レイアウト

    // ViewModel関連の依存関係
    // UIのデータ管理とライフサイクル対応
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.activity:activity-ktx:1.8.2")

    // Retrofit関連の依存関係
    // HTTPクライアントライブラリ
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Coroutines関連の依存関係
    // 非同期処理用ライブラリ
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

    // あとから追加Httpのログのライブラリ？
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // 画像読み込みライブラリ
    implementation("com.github.bumptech.glide:glide:4.16.0")

    // テスト関連の依存関係
    testImplementation(libs.junit)  // ユニットテスト用
    androidTestImplementation(libs.androidx.junit)  // Android用テスト
    androidTestImplementation(libs.androidx.espresso.core)  // UIテスト用


}

/* リファクタリングの提案:
1. バージョン管理の改善
   - ライブラリのバージョンを一元管理するために、libs.versionsファイルを作成することを推奨
   - 特に、ViewModel、Retrofit、Coroutinesのバージョンを集中管理すべき

2. BuildType設定の拡張
   - debugビルドタイプの明示的な設定を追加することを検討
   - 開発環境用の設定（デバッグログの有効化など）を追加

3. 依存関係の整理
   - implementation(libs.*)の形式に統一することを推奨
   - カスタムカタログバージョンの導入を検討

4. ProGuard設定の見直し
   - リリースビルド時のコード最適化（isMinifyEnabled）を有効にすることを検討
   - 必要に応じてR8の設定を追加

5. compileSdkとtargetSdkの更新
   - 最新のSDKバージョンに更新することを検討
   - 定期的な更新計画の策定を推奨
*/