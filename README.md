# myjpnparser  
  
## Зависимости:  
  
oracle java 12  
perl  
perl moreutils  
tesseract ocr  
maven  
imagemagick  
translate-shell https://github.com/soimort/translate-shell  
  
`sudo apt-get install perl tesseract-ocr-jpn tesseract-ocr-jpn-vert maven liblist-moreutils-perl imagemagick`
  
  
## Установка:  

Установить требуемые зависимости  
Скачать translate-shell и закинуть все его файлы в ./bin/bash/translate_shell  
Пометить ./bin/bash/translate_shell/translate как исполняемый  

Запустить geany.pl c ключем --project  
  
`perl geany.pl --project`  

Запустить geany.pl c ключем --libupd  
  
`perl geany.pl --libupd`  
  

Запустить geany.pl c ключем --make  
  
`perl geany.pl --make`  
  
  
## Как пользоваться:  
  
Повесить скрипт ./bin/perl/recognize_and_send_by_tesseract.pl на удобный вам хоткей  
Запустить geany.pl c ключем --run  

`perl geany.pl --run`  
  
Используйте хоткей для запуска скрипта и выделяйте нужный вам текст  
  
## ocr.space (опционально)

Онлайн парсер, который в некоторых местах может справиться лучше чем tesseract  
Чтобы начать им пользоваться, нужно получить api ключ на https://ocr.space/  
Полученный ключ, записать в файл ./bin/perl/ocrspace_apikey  
Повесить скрипт ./bin/perl/recognize_and_send_by_ocrspace.pl на удобный хоткей
