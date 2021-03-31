# Darts Training Calculator (Work In Progress)

The app counts scores during Darts training session.

To-do:
- в Настройках выбор "начинать с удвоения" (doubling in ON/OFF) (тогда будет иметь смысл создать data class GameSettings и передавать в ViewModel его)
- когда игра закончена, показывать кнопку "Новая игра" в ScoreFragment

Improvements:
- добавить возможность кастомной игры (301, 501, введите своё значение (>1))
- проверить как сохранять одним запросом, используя foreign key
- мне не нравится, что приходится передавать клики юзера из ViewHolder во фрагмент через Adapter

*Database diagram:*

<img src="docs/dtc_database_diagram.png" width="800"/>
