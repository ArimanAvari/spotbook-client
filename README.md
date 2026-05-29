# SpotBook Android Client

Android-клиент курсового проекта «Личный путеводитель».

Приложение создаётся на Kotlin. Сейчас добавлен базовый Android-проект с Jetpack Compose, Material 3 и Navigation Compose.

## Запуск

Проект открывается в Android Studio как обычный Android/Gradle-проект.

Для проверки сборки:

```powershell
.\gradlew.bat assembleDebug
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

Разработка ведётся в отдельной ветке `android-dev`.
