# myjpnparser  
  
## Зависимости:  
  
oracle java 12  
perl  
perl moreutils  
tesseract ocr  
maven  
translate-shell https://github.com/soimort/translate-shell  
  
`sudo apt-get install perl tesseract-ocr-jpn tesseract-ocr-jpn-vert maven liblist-moreutils-perl`
  
  
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
