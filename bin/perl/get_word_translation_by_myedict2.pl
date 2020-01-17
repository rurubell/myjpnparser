#!/usr/bin/perl
#Получить переводы для слова из myedict2.xml


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


#Путь до myedict2.xml
my $S_MYEDICT2_PATH = $S_SELF_DIR_PATH . "../../res/xml/myedict2.xml";


my $s_command = " grep -m 1 \"<word>" . $S_WORD . "</word>\" " . $S_MYEDICT2_PATH;
my $s_result = `$s_command`;
chomp( $s_result );

if( length( $s_result ) <= 0 )
{
	$s_command = " grep -m 1 \"<read>" . $S_WORD . "</read>\" " . $S_MYEDICT2_PATH;
	$s_result = `$s_command`;
	chomp( $s_result );
}

print $s_result . "\n";
