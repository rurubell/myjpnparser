#!/usr/bin/perl


#Скрипт для поиска похожих слов


use strict;
use warnings;
use utf8;
use Cwd 'abs_path';
use Scalar::Util qw( looks_like_number );
use Encode qw(decode encode);
use Cwd 'abs_path';
use List::MoreUtils qw(uniq);

binmode(STDOUT,':utf8');
binmode(STDIN,':utf8');


#Слово, для которого ищем похожие
my $S_WORD = decode( "utf-8", $ARGV[ 0 ] );
if( length( $S_WORD ) <= 0 ) { exit; }


#Путь до скрипта
my $S_SELF_PATH = abs_path( $0 );
#Путь до директории скрипта
my $S_SELF_DIR_PATH = `dirname $S_SELF_PATH`;
chomp( $S_SELF_DIR_PATH );
$S_SELF_DIR_PATH = $S_SELF_DIR_PATH . "/";


#Путь до файла-словаря
my $S_DICT_PATH = $S_SELF_DIR_PATH . "../../res/txt/japan_words";



$S_WORD =~ s/\*/\./gi;
my $s_command = "grep \"^" . $S_WORD . "\$\" " . $S_DICT_PATH;
my @s_sim = `$s_command`;
chomp( @s_sim );


@s_sim = uniq( @s_sim );
for( my $i = 0; $i < @s_sim; $i++ )
{
	$s_sim[ $i ] = decode( "utf8", $s_sim[ $i ] );
	if( ( $i + 1 ) == @s_sim ) { print( $s_sim[ $i ] ); }
	else { print( $s_sim[ $i ] . "::" ); }
}

print( "\n" );
