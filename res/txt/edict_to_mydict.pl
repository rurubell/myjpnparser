#!/usr/bin/perl


#Скрипт для перегона edict2_utf8 файла словаря, в более удобочитаемый формат


use strict;
use warnings;
use Scalar::Util qw( looks_like_number );
use IO::Socket::INET;
use utf8;

use open ':std', ':encoding(UTF-8)';
binmode STDOUT, ":utf8";


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

#Слово состоит только из хираганы?
sub isOnlyHiragana
{
	my $s_word = shift();
	my $s_kana = "まあぁいぃうゔぅえぇおぉかがきぎくぐけげこごさざしじすずせぜそぞただちぢつづっってでとどなにぬねのはばぱひびぴふぶぷへべぺほぼぽまみむめもやゃゆゅよょらりるれろわゎをんゐーゝゞ";
	
	for( my $i = 0; $i < length( $s_word ); $i++ )
	{
		my $s_char = substr( $s_word, $i, 1 );
		if( index( $s_kana, $s_char ) == -1 ) { return 0; }
	}
	
	return 1;
}


#Слово состоит только из катаканы?
sub isOnlyKatakana
{
	my $s_word = shift();
	my $s_kana = "マアァイィウヴゥエェオォカガヵキギクグケゲヶコゴサザシジスズセゼソゾタダチヂツヅッテデトドナニヌネノハバパヒビピフブプヘベペホボポマミムメモヤャユュヨョラリルレロワヮヲンヰ・ーヽヾ";
	
	for( my $i = 0; $i < length( $s_word ); $i++ )
	{
		my $s_char = substr( $s_word, $i, 1 );
		if( index( $s_kana, $s_char ) == -1 ) { return 0; }
	}
	
	return 1;
}


#Читаем файл
my @s_strings = readUTF8FileAsStringArr( "./edict2_utf8" );


for( my $i = 0; $i < @s_strings; $i++ )
{
	my @s_keys;
	#Получить все слова - ключи (кандзи)
	my $s_kanji_string = $s_strings[ $i ];
	$s_kanji_string =~ s/\s.*//gi;
	#Удалить все между () вместе с ()
	$s_kanji_string =~ s/\(.*?\)//gi;
	my @s_kanji_words = split( ";", $s_kanji_string );
	
	my @s_kana_words;
	#Получаем всю кану
	if( index( $s_strings[ $i ], "[" ) == -1 ) {}
	else
	{
		my $n_start_index = index( $s_strings[ $i ], "[" ) + 1;
		my $n_end_index = index( $s_strings[ $i ], "]", $n_start_index );
		
		my $s_kana_string = substr( $s_strings[ $i ], $n_start_index, $n_end_index - $n_start_index );
		@s_kana_words = split( ";", $s_kana_string );
	}
	
	#Получаем перевод
	my $n_start_index = index( $s_strings[ $i ], "/" ) + 1;
	my $s_translation = substr( $s_strings[ $i ], $n_start_index );
	#Удалить все после /EntL
	$s_translation =~ s/\/EntL.*//gi;
	
	#Удалить теги
	$s_translation =~ s/\(n\)//gi;
	$s_translation =~ s/\(n\,.*?\)//gi;
	$s_translation =~ s/\(adj.*?\)//gi;
	$s_translation =~ s/\(exp.*?\)//gi;
	$s_translation =~ s/\(int.*?\)//gi;
	$s_translation =~ s/\(v5.*?\)//gi;
	$s_translation =~ s/\(adv.*?\)//gi;
	$s_translation =~ s/\(abbr.*?\)//gi;
	$s_translation =~ s/\(conj.*?\)//gi;
	$s_translation =~ s/\(aux.*?\)//gi;
	$s_translation =~ s/\(yoji.*?\)//gi;
	$s_translation =~ s/\(yoji.*?\)//gi;
	$s_translation =~ s/\(See.*?\)//gi;
	$s_translation =~ s/\(on-.*?\)//gi;
	$s_translation =~ s/\(esp\..*?\)//gi;
	$s_translation =~ s/\(n-.*?\)//gi;
	$s_translation =~ s/\(v1.*?\)//gi;
	$s_translation =~ s/\(vs-.*?\)//gi;
	$s_translation =~ s/\(uk\)//gi;
	$s_translation =~ s/\(pol\)//gi;
	$s_translation =~ s/\(sl\)//gi;
	$s_translation =~ s/\(col\)//gi;
	$s_translation =~ s/\(suf\)//gi;
	$s_translation =~ s/\(prt\)//gi;
	$s_translation =~ s/\(hum\)//gi;
	$s_translation =~ s/\(P\)//gi;
	$s_translation =~ s/  / /gi;
	$s_translation =~ s/^\s+//;
	
	#Исправить номера
	$s_translation =~ s/\/\(/ \(/gi;
	$s_translation =~ s/\//\; /gi;
	
	
	#Если в строке только кандзи (слово только из каны)
	if( @s_kana_words == 0 )
	{
		for( my $j = 0; $j < @s_kanji_words; $j++ )
		{
			print( "<word>" . $s_kanji_words[ $j ] . "</word>" );
			print( "<read>" . $s_kanji_words[ $j ] . "</read>" );
			print( "<transl>" . $s_translation . "</transl>\n" );
		}
	}
	#Если у нас только одно кандзислово и одно чтение
	elsif( ( @s_kanji_words == 1 ) && ( @s_kana_words == 1 ) )
	{
		for( my $j = 0; $j < @s_kana_words; $j++ )
		{
			$s_kana_words[ $j ] =~ s/\(.*?\)//gi;
			print( "<word>" . $s_kanji_words[ 0 ] . "</word>" );
			print( "<read>" . $s_kana_words[ $j ] . "</read>" );
			print( "<transl>" . $s_translation . "</transl>\n" );
		}
	}
	#Если несколько кандзислов но одно чтение
	elsif( ( @s_kanji_words > 1 ) && ( @s_kana_words == 1 ) )
	{
		for( my $j = 0; $j < @s_kanji_words; $j++ )
		{
			$s_kana_words[ 0 ] =~ s/\(.*?\)//gi;
			print( "<word>" . $s_kanji_words[ $j ] . "</word>" );
			print( "<read>" . $s_kana_words[ 0 ] . "</read>" );
			print( "<transl>" . $s_translation . "</transl>\n" );
		}
	}
	#Если несколько чтений но одно кандзи
	elsif( ( @s_kanji_words == 1 ) && ( @s_kana_words > 1 ) )
	{
		for( my $j = 0; $j < @s_kana_words; $j++ )
		{
			$s_kana_words[ 0 ] =~ s/\(.*?\)//gi;
			print( "<word>" . $s_kanji_words[ 0 ] . "</word>" );
			print( "<read>" . $s_kana_words[ $j ] . "</read>" );
			print( "<transl>" . $s_translation . "</transl>\n" );
		}
	}
	#Если несколько чтений и несколько кандзи
	elsif( ( @s_kanji_words > 1 ) && ( @s_kana_words > 1 ) )
	{
		for( my $j = 0; $j < @s_kanji_words; $j++ )
		{
			for( my $k = 0; $k < @s_kana_words; $k++ )
			{
				if( index( $s_kana_words[ $k ], $s_kanji_words[ $j ] ) != -1 )
				{
					my $s_kana = $s_kana_words[ $k ];
					$s_kana =~ s/\(.*?\)//gi;
					print( "<word>" . $s_kanji_words[ $j ] . "</word>" );
					print( "<read>" . $s_kana . "</read>" );
					print( "<transl>" . $s_translation . "</transl>\n" );
					goto MET
				}
			}
			
			my $s_kana = $s_kana_words[ 0 ];
			$s_kana =~ s/\(.*?\)//gi;
			print( "<word>" . $s_kanji_words[ $j ] . "</word>" );
			print( "<read>" . $s_kana . "</read>" );
			print( "<transl>" . $s_translation . "</transl>\n" );
			
			MET:
		}
	}
}
