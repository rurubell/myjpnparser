#!/usr/bin/perl


#Скрипт для перегонки xml в sql


use strict;
use warnings;
use utf8;
use Scalar::Util qw( looks_like_number );
use Encode qw(decode encode);
use Cwd 'abs_path';
use File::Basename;
use Term::ANSIColor;

binmode(STDOUT,':utf8');
binmode(STDIN,':utf8');


#Путь до XML файла
my $S_XML_PATH = $ARGV[ 0 ];

#Типы полей в xml файле
my @s_tags_arr = ( "knj", "kan", "en", "ru" );
#Типы полей в sql файле
my @s_flds_nms = ( "kanji", "kana", "eng_translation", "ru_translation" );
#Имя создаваемой таблицы
my $s_table_name = "my_jmdict";


#Get utf8 file as array of lines
sub readUTF8FileAsStringArr
{
	my $s_file_path = shift();
	open( my $file, '<:encoding( utf8 )', $s_file_path ) or die "Can't open file " . $s_file_path . "!\n";
	
	my @s_arr_of_lines = <$file>;
	chomp( @s_arr_of_lines );
	
	close( $file );
	return @s_arr_of_lines;
}


#Получить значение тега
sub getTagValue
{
	my $s_str = shift();
	my $s_tag = shift();
	
	my $s_start_tag = "<" . $s_tag . ">";
	my $s_end_tag = "</" . $s_tag . ">";
	my $n_start = index( $s_str, $s_start_tag ) + length( $s_start_tag );
	my $n_end = index( $s_str, $s_end_tag, $n_start );
	
	my $s_res = substr( $s_str, $n_start, $n_end - $n_start );
	return $s_res;
}


my @s_file_strings = readUTF8FileAsStringArr( $S_XML_PATH );

print( "DROP TABLE " . $s_table_name . ";\n\n" );
print( "BEGIN TRANSACTION;\n" );

print( "CREATE TABLE " . $s_table_name . "( " );

for( my $i = 0; $i < @s_flds_nms; $i++ )
{
	if( ( $i + 1 ) < @s_flds_nms ) { print( "\"" . $s_flds_nms[ $i ] . "\" TEXT, " ); }
	else { print( "\"" . $s_flds_nms[ $i ] . "\" TEXT " ); }
}

print( ");\n\n" );

for( my $i = 0; $i < @s_file_strings; $i++ )
{
	my $s_string = $s_file_strings[ $i ];
	$s_string =~ s/\'//gi;
	
	print( "INSERT INTO \"" . $s_table_name . "\" VALUES( " );
	for( my $j = 0; $j < @s_tags_arr; $j++ )
	{
		if( ( $j + 1 ) < @s_tags_arr ) { print( "\'" . getTagValue( $s_string, $s_tags_arr[ $j ] ) . "\', " ); }
		else { print( "\'" . getTagValue( $s_string, $s_tags_arr[ $j ] ) . "\' " ); }
	}
	print( ");\n" );
}
print( "COMMIT;\n" );
