#!/usr/bin/perl
#Получить переводы для слова из JMDICT.XML


use strict;
use warnings;
use utf8;
use Cwd 'abs_path';
use Scalar::Util qw( looks_like_number );
use Encode qw(decode encode);
use Cwd 'abs_path';

#binmode(STDOUT,':utf8');
#binmode(STDIN,':utf8');


#Путь до скрипта
my $S_SELF_PATH = abs_path( $0 );
#Путь до директории скрипта
my $S_SELF_DIR_PATH = `dirname $S_SELF_PATH`;
chomp( $S_SELF_DIR_PATH );
$S_SELF_DIR_PATH = $S_SELF_DIR_PATH . "/";


#Единственный параметр - японское слово
my $S_WORD = decode( "utf-8", $ARGV[ 0 ] );


#Путь до JMDICT.XML
my $S_JMDICT_PATH = "./JMDICT.XML";


#Print all array values
sub printArr
{
	my @s_arr = @{ shift() };
	chomp( @s_arr );
	
	for( my $i = 0; $i < @s_arr; $i++ )
	{
		print( $s_arr[ $i ] . "\n" );
	}
}


#Получить позицию строки в файле, начиная с оффсета
sub getStringPosByOffset
{
	my $s_file_path = shift();
	my $s_pattern = shift(); #То что ищем...
	my $n_offset = shift();
	
	my @s_tmp = `tail -n +$n_offset $s_file_path | grep -n "$s_pattern" | cut -d : -f 1`;
	chomp( @s_tmp );
	
	if( @s_tmp == 0 ) { return ""; }
	return $s_tmp[ 0 ] + $n_offset;
}


#Получить строки файла от позиции до оффсета
sub getFilePiece
{
	my $s_file_path = shift();
	my $n_start = shift();
	my $n_offset = shift();
	
	my @s_res = `tail -n +$n_start $s_file_path | head -n$n_offset`;
	chomp( @s_res );
	
	return @s_res;
}


#Получить все переводы для слова
sub getTranslationsForWord
{
	my @s_xml_blob = @{ shift() };
	my @s_res;
	
	for( my $i = 0; $i < @s_xml_blob; $i++ )
	{
		if( index( $s_xml_blob[ $i ], "<gloss>" ) != -1 )
		{
			my $n_start = index( $s_xml_blob[ $i ], "<gloss>" ) + length( "<gloss>" );
			my $n_end = index( $s_xml_blob[ $i ], "</gloss>" );
			my $s_transl = substr( $s_xml_blob[ $i ], $n_start, $n_end - $n_start );
			push( @s_res, $s_transl );
		}
	}
	
	return @s_res;
}


#Получаем позицию строки, которая содержит наше слово
$S_WORD = ">" . $S_WORD . "<";
my $n_xml_block_start_pos = getStringPosByOffset( $S_JMDICT_PATH, $S_WORD, 0 );
if( length( $n_xml_block_start_pos ) <= 0 ) { print "none\n"; exit; }
my $n_xml_block_stop_pos = getStringPosByOffset( $S_JMDICT_PATH, "</entry>", $n_xml_block_start_pos );

#Получаем XML блоб для слова
my @s_strings = getFilePiece( $S_JMDICT_PATH, $n_xml_block_start_pos, $n_xml_block_stop_pos - $n_xml_block_start_pos );
my @s_translations = getTranslationsForWord( \@s_strings );
printArr( \@s_translations );

