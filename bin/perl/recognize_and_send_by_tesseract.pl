#!/usr/bin/perl


use strict;
use warnings;
use Scalar::Util qw( looks_like_number );
use IO::Socket::INET;
use utf8;

use open ':std', ':encoding(UTF-8)';
binmode STDOUT, ":utf8";


#Путь до TMP
my $S_TMP_PATH = "/tmp/";
#Путь до папке с картинками
my $S_IMAGE_PATH = $S_TMP_PATH . "jwrdmem/";
#socket port
my $S_PORT = "7777";
#Ширина рамки вокруг картинки (пикселов)
my $S_PIC_BORDER = 5;

#Макимальная ширина картинки (пикселов)
my $N_MAX_IMAGE_WIDTH = 800;
#Максимальная высота картинки (пикселов)
my $N_MAX_IMAGE_HEIGHT = 800;


system( "mkdir -p " . $S_IMAGE_PATH );


#Make random string
sub makeRandomString
{
	my $n_strlen = shift();
	my $s_alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	my $s_res = "";
	
	if( !looks_like_number( $n_strlen ) ) { die( (caller(0))[3] . " \$n_strlen = " . $n_strlen . " - not a number!\n" ); }
	if( int( $n_strlen ) <= 0 ) { die( (caller(0))[3] . " \$n_strlen = " . $n_strlen . " <= 0!\n" ); }
	
	for( my $i = 0; $i < $n_strlen; $i++ )
	{
		my $n_rand_pos = int( rand( length( $s_alpha ) ) );
		$s_res .= substr( $s_alpha, $n_rand_pos, 1 );
	}
	
	return $s_res;
}


#Send string to server
sub sendStringToServer
{
	my $s_req = shift();
	
	# create a connecting socket
	my $socket = new IO::Socket::INET 
	(
		PeerHost => '127.0.0.1',
		PeerPort => $S_PORT,
		Proto => 'tcp',
	);
	die "cannot connect to the server $!\n" unless $socket;
	print "connected to the server\n";
	binmode( $socket, ":utf8" );
	$socket->send( $s_req );
}


#Имя файла
my $s_name = $S_IMAGE_PATH . makeRandomString( 10 ) . ".png";
#Выделяем текст
my $s_import_command = "import -verbose " . $s_name . " 2>&1";
my $s_import_out = `$s_import_command`;
chomp( $s_import_out );

#Получаем ширину, высоту, смещение по горизонтали и вертикали изображения
my $n_width = 0;
my $n_height = 0;
my $n_voffset = 0;
my $n_hoffset = 0;

my $s_tmp = $s_import_out;
$s_tmp = substr( $s_tmp, index( $s_tmp, "PS " ) + length( "PS " ) );
$s_tmp = substr( $s_tmp, 0, index( $s_tmp, " " ) );

$n_width = substr( $s_tmp, 0, index( $s_tmp, "x" ) );
$n_height = substr( $s_tmp, index( $s_tmp, "x" ) + 1 );

if( ( $n_width > $N_MAX_IMAGE_WIDTH ) || ( $n_height > $N_MAX_IMAGE_HEIGHT ) ) 
{
	system( "rm " . $s_name );
	die "Image is TOO big!\n"; 
}

$s_tmp = $s_import_out;
$s_tmp = substr( $s_tmp, index( $s_tmp, "+" ) + 1 );
$s_tmp = substr( $s_tmp, 0, index( $s_tmp, " " ) );

$n_hoffset = substr( $s_tmp, 0, index( $s_tmp, "+" ) );
$n_voffset = substr( $s_tmp, index( $s_tmp, "+" ) + 1 );


#Добавляем рамку
system( "convert " . $s_name . " -bordercolor white -border " . $S_PIC_BORDER . " " . $s_name );

#Tesseract command
my $s_tesseract_command = "tesseract " . $s_name . " stdout -l jpn_vert --psm 5";

#Получаем распарсенный текст
my @s_parsed_strings = `$s_tesseract_command`;
my $s_parsed_text = "";

for( my $i = 0; $i < @s_parsed_strings; $i++ )
{
	if( length( $s_parsed_strings[ $i ] ) <= 1 ) { next; }
	my $s_buf = $s_parsed_strings[ $i ];
	chomp( $s_buf );
	$s_parsed_text .= $s_buf . "\n";
}

$s_parsed_text = substr( $s_parsed_text, 0, length( $s_parsed_text ) - 1 );

 
$s_parsed_text =~ s/ //gi;
$s_parsed_text =~ s/　//gi;


print $s_parsed_text . "\n";
sendStringToServer
( 
	"<text>" . $s_parsed_text . "</text>" .
	"<width>" . $n_width . "</width>" .
	"<height>" . $n_height . "</height>" .
	"<hoffset>" . $n_hoffset . "</hoffset>" .
	"<voffset>" . $n_voffset . "</voffset>" .
	"<ocrtype>" . "tesseract" . "</ocrtype>"
);
