# SpotBook Android Client

Android-клиент курсового проекта «Личный путеводитель».

Приложение создаётся на Kotlin. Сейчас добавлен базовый Android-проект с Jetpack Compose, Material 3 и Navigation Compose.

## Запуск

Проект открывается в Android Studio как обычный Android/Gradle-проект.

Для проверки сборки:

```powershell
.\gradlew.bat assembleDebug
```

Backend по умолчанию ожидается по адресу `http://10.0.2.2:8080`, это удобно для Android Emulator. Для реального устройства можно собрать debug APK с другим адресом:

```powershell
.\gradlew.bat assembleDebug -PbackendBaseUrl=http://192.168.1.10:8080
```

## Стек

- Jetpack Compose;
- Navigation Compose;
- ViewModel;
- Room;
- Clean Architecture;
- ручная синхронизация с backend.

## Структура

Основной пакет:

```text
app/src/main/java/com/spotbook/personalguide
```

Заложены слои:

- `data`;
- `domain`;
- `presentation`.

## Локальная база данных

Добавлен Room. Локальная база называется:

```text
spotbook_local.db
```

Таблицы:

- `local_place_cards`;
- `local_groups`.

Для карточек и групп есть DAO, Entity, доменные модели и мапперы между слоями.

## Экраны

Сейчас реализованы Compose-экраны:

- вход;
- регистрация;
- список карточек;
- создание карточки;
- детали карточки;
- редактирование карточки;
- список групп;
- карточки внутри группы;
- синхронизация.

Карточки и группы пока работают с локальной Room-базой. Подключение к backend будет добавлено следующим этапом.

## Backend

Экран входа и регистрации подключён к Ktor backend. JWT-токен сохраняется локально в `SharedPreferences` и используется для защищённых запросов.

Разработка ведётся в отдельной ветке `android-dev`.
