#!/usr/bin/perl

#Скрипт сборки проекта, в качестве параметра, принимает цель
#Типы целей:
#	--project - переконфигурировать проект ( необходимо прогнать при разворачивании на новой машине или в новой директории )
#	--make - собрать проект
#	--run - запустить проект
#	--arch - архивировать проект в ./arch


use strict;
use warnings;
use utf8;
use List::MoreUtils qw( uniq );

use Cwd 'abs_path';


#Путь до скрипта
my $S_SELF_PATH = abs_path( $0 );
#Имя скрипта
my $S_SELF_NAME = `basename $S_SELF_PATH`;
chomp( $S_SELF_NAME );
#Путь до директории скрипта
my $S_SELF_DIR_PATH = `dirname $S_SELF_PATH`;
chomp( $S_SELF_DIR_PATH );
$S_SELF_DIR_PATH = $S_SELF_DIR_PATH . "/";

#Подключаем вспомогательные скрипты
require $S_SELF_DIR_PATH . ".geany/pl/MyArrayUtils.pl";
require $S_SELF_DIR_PATH . ".geany/pl/MyFileUtils.pl";

#Переходим в директорию скрипта
chdir( $S_SELF_DIR_PATH ) or die "Не могу зайти в директорию " . $S_SELF_DIR_PATH . " !\n";

system( "mkdir ./lib/" );
system( "mkdir ./bin/bash/translat_eshell" );

#Путь до директории с сорцами
my $S_SOURCES_DIR_PATH = healDirPath( $S_SELF_DIR_PATH . "java/" );
#Путь до директории с либами
my $S_LIB_DIR_PATH = healDirPath( $S_SELF_DIR_PATH . "lib/" );
#Путь до директории с временными файлами
my $S_TMP_DIR = healDirPath( $S_SELF_DIR_PATH . "tmp/" );
#Путь до файла, хранящего хешкоды каждого сорца
my $S_SOURCES_HASH_FILE_PATH = $S_TMP_DIR . "src_hash";
#Путь до файла, хранящего список файлов которые необходимо собрать
my $S_MAKE_LIST_FILE = $S_TMP_DIR . "make_list";
#Путь до директории с архивами
my $S_ARCH_DIR_PATH = healDirPath( $S_SELF_DIR_PATH . "arch/" );

#Путь до выходного jar-ника
my $S_OUT_JAR_FILE_NAME = $S_SELF_DIR_PATH . "myjpnparser.jar";
#Путь до MANIFEST
my $MANIFEST_PATH = $S_TMP_DIR . "MANIFEST";
#Директория для файлов class
my $S_CLASS_DIR_PATH = healDirPath( $S_TMP_DIR . "_build/" );
#Команда сборки
my $S_BUILD_OBJ_COMMAND = "javac @" . $S_MAKE_LIST_FILE . " -d " . $S_CLASS_DIR_PATH . " -classpath " . $S_CLASS_DIR_PATH . makeClassPathLibsString();

#java main-class
my $S_JAVA_MAIN_CLASS = "src.MyMain";

#Путь до конфигурационного файла
my $S_MYJPNPARSER_CONF_FILE_PATH = healDirPath( $S_SELF_DIR_PATH . "conf/" ) . "main.conf";


#Цель
my $S_PURPOSE = $ARGV[ 0 ];
#Команда запуска
my $S_RUN_COMMAND = 
	"java -Dawt.useSystemAAFontSettings=on -Dswing.defaultlaf=com.sun.java.swing.plaf.gtk.GTKLookAndFeel -Xmx512m -XX:+UseG1GC -XX:MinHeapFreeRatio=20 -XX:MaxHeapFreeRatio=40 " .
	"-jar " . $S_OUT_JAR_FILE_NAME . " " .
	"--conf_path " . $S_MYJPNPARSER_CONF_FILE_PATH;


#Создаем нужные директории ( если нет )
`mkdir -p $S_CLASS_DIR_PATH`;


#Получить список всех файлов исходников ( .java )
sub getListOfAllSrcFiles
{
	my @s_res;
	my @s_java_files_list = findFilesInDirRecursive( "*.java", $S_SOURCES_DIR_PATH );
	push( @s_res, @s_java_files_list );
	chomp( @s_res );
	
	return @s_res;
}


#Получить список всех файлов библиотек ( .jar )
sub getListOfAllJarFiles
{
	my @s_res;
	my @s_jar_files_list = findFilesInDirRecursive( "*.jar", $S_LIB_DIR_PATH );
	push( @s_res, @s_jar_files_list );
	chomp( @s_res );
	
	return @s_res;
}


#Получить строку либ для classpath
sub makeClassPathLibsString
{
	my @s_jar = getListOfAllJarFiles();
	my $s_res = "";
	
	if( @s_jar != 0 )
	{
		for( my $i = 0; $i < @s_jar; $i++ ) { $s_res .= ":" . $s_jar[ $i ]; }
	}
	
	return $s_res;
}


#Обновляем все хеши
sub updateAllHashes
{
	`cat /dev/null > $S_SOURCES_HASH_FILE_PATH`;
	#Получаем списки всех .java файлов
	my @s_src_files = getListOfAllSrcFiles();
	
	for( my $i = 0; $i < @s_src_files; $i++ )
	{
		`md5sum $s_src_files[ $i ] >> $S_SOURCES_HASH_FILE_PATH`;
	}
}


#Получить список файлов, в которых есть изменения
sub getListOfChangedFiles
{
	my @s_old_hashes = readTextFileAsStringsArr( $S_SOURCES_HASH_FILE_PATH );
	my @s_src_files = getListOfAllSrcFiles();
	my @s_res;
	
	for( my $i = 0; $i < @s_src_files; $i++ )
	{
		my $n_flag = 0;
		my $s_new_hash = `md5sum $s_src_files[ $i ]`;
		chomp( $s_new_hash );
		for( my $j = 0; $j < @s_old_hashes; $j++ )
		{
			if( index( $s_old_hashes[ $j ], $s_new_hash ) != -1 )
			{
				$n_flag = 1;
				last;
			}
		}
		if( $n_flag == 0 ) { push( @s_res, $s_src_files[ $i ] ); }
	}
	
	return @s_res;
}


#Создать MAKEFILE
sub createMAKEFILE
{
	system( "cat /dev/null > " . $MANIFEST_PATH );
	my @s_manifest_strings;
	
	push( @s_manifest_strings, "Manifest-Version: 1.0" );
	push( @s_manifest_strings, "Created-By: 1.8.0_72 (Oracle Corporation)" );
	push( @s_manifest_strings, "Main-Class: " . $S_JAVA_MAIN_CLASS );
	
	#Создаем строку - набор либ
	my $s_libs_string = "Class-Path: ";
	my @s_libs = findFilesInDirRecursive( "*.jar", $S_LIB_DIR_PATH );
	
	for( my $i = 0; $i < @s_libs; $i++ ) 
	{ 
		$s_libs_string .= "./" . `realpath --relative-to=./ $s_libs[ $i ]`;
		chomp( $s_libs_string );
		$s_libs_string .= " ";
	}
	
	push( @s_manifest_strings, $s_libs_string );
	writeStringArrToNewFile( $MANIFEST_PATH, \@s_manifest_strings );
}


#Сборка
sub make
{
	my @s_files = getListOfChangedFiles();
	my $n_errcode = 0;
	if( @s_files <= 0 ) { return; }
	
	writeStringArrToNewFile( $S_MAKE_LIST_FILE, \@s_files );
	$n_errcode += system( $S_BUILD_OBJ_COMMAND );
	
	if( $n_errcode == 0 ) 
	{ 
		updateAllHashes();
		chdir( $S_CLASS_DIR_PATH );
		system( "jar cfm0 " . $S_OUT_JAR_FILE_NAME . " " . $MANIFEST_PATH . " ./*" );
		chdir( $S_SELF_PATH );
	}
}


#переконфигурировать проект
sub project
{
	#Получаем список всех поддиректорий в папке с сырцами
	my @s_dirs = `find $S_SOURCES_DIR_PATH -type d`;
	chomp( @s_dirs );
	printArr( \@s_dirs );
	
	system( "ln -f -s " . $S_SELF_PATH . " " . $S_SOURCES_DIR_PATH . $S_SELF_NAME );
	for( my $i = 0; $i < @s_dirs; $i++ )
	{
		system( "ln -f -s " . $S_SELF_PATH . " " . healDirPath( $s_dirs[ $i ] ) . $S_SELF_NAME );
	}
	
	system( "cat /dev/null > " . $S_SOURCES_HASH_FILE_PATH );
	system( "rm -rf " . $S_CLASS_DIR_PATH . "*" );
	system( "rm -rf " . $S_OUT_JAR_FILE_NAME . "*" );
	
	createMAKEFILE();
}


#Запуск проекта
sub run
{
	system( $S_RUN_COMMAND );
}


#Скачать/обновить либы
sub updateLibs
{
	system( "mvn clean dependency:copy-dependencies" );
}


#Архивировать проект
sub archiveProject
{
	my $s_arch_name = `date +%F_%H:%M`;
	chomp( $s_arch_name );
	$s_arch_name .= ".zip";
	
	print "Make new archive: " . $s_arch_name . "\n";
	
	system( "zip -q -r " . $S_ARCH_DIR_PATH . $s_arch_name . " ./ -x /arch/**\*" );
}


sub main
{
	if( index( $S_PURPOSE, "--project" ) != -1 ) { project(); }
	elsif( index( $S_PURPOSE, "--run" ) != -1 ) { run(); }
	elsif( index( $S_PURPOSE, "--make" ) != -1 ) { make(); }
	elsif( index( $S_PURPOSE, "--libupd" ) != -1 ) { updateLibs(); }
	elsif( index( $S_PURPOSE, "--arch" ) != -1 ) { archiveProject(); }
	else { print "Неизветная цель " . $S_PURPOSE . "\n"; }
}


main();
