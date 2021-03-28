# Darts Training Calculator (Work In Progress)

The app counts scores during Darts training session.

To-do:

- добавить возможность кастомной игры (301, 501, введите своё значение (>1))
- в Настройках выбор "начинать с удвоения" (doubling in ON/OFF)
- добавить дополнительное поле в SavedGames - кол-во бросков и показывает его в History
- броски, которые не пошли в зачёт, должны отличаться (например, зачёркиваться)
- (может быть) добавить удаление бросков с конца списка
- проверить как сохранять одним запросом, используя foreign key
- формат даты из настроек телефона
- мне не нравится, что приходится передавать клики юзера из ViewHolder во фрагмент через Adapter

*Database diagram:*

<img src="docs/dtc_database_diagram.png" width="800"/>
