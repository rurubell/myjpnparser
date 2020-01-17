#!/usr/bin/perl

use strict;
use warnings;


#Add '/' to the end of path, if it doesn't have it
sub healDirPath
{
	my $s_path = shift();
	if( length( $s_path ) <= 0 ) { die( (caller(0))[3] . " \$s_path has a zero length!\n" ); }
	
	if( index( substr( $s_path, length( $s_path ) - 1, 1 ), '/' ) != -1 ) { return $s_path; }
	else { return $s_path . "/"; }
}


#Read text file and return it as array of strings
sub readTextFileAsStringsArr
{
	my $s_file_path = shift();
	if( !( -e $s_file_path ) ) { die( (caller(0))[3] . " File " . $s_file_path . ", not found!\n" ); }
	
	my @s_res = `cat $s_file_path`;
	chomp( @s_res );
	
	return @s_res;
}


#Find string in file
#return position of string or -1
sub isFileContainsString
{
	my $s_string = shift();
	my $s_file_path = shift();
	if( !( -e $s_file_path ) ) { die( (caller(0))[3] . " " . $s_file_path . " - doesn't exists!\n" ); }
	
	my @s_file = readFileAssStringsArr( $s_file_path );
	for( my $i = 0; $i < @s_file; $i++ )
	{
		if( index( $s_file[ $i ], $s_string ) != -1 ) { return $i; }
	}
	
	return -1;
}


#Get list of all files in dir (and subdirectories), by mask
sub findFilesInDirRecursive
{
	my $s_mask = shift();
	my $s_dir = healDirPath( shift() );
	if( !( -e $s_dir ) ) { die( (caller(0))[3] . " " . $s_dir . " - doesn't exists!\n" ); }
	if( !( -d $s_dir ) ) { die( (caller(0))[3] . " " . $s_dir . " - not a directory!\n" ); } 
	
	my $s_command = "find " . $s_dir . " -type f -iname \'" . $s_mask . "\'";
	my @s_res = `$s_command`;
	chomp( @s_res );
	
	return @s_res;
}


#Get list of all subdirs for dir
sub findSubdirsRecursive
{
	my $s_dir = healDirPath( shift() );
	if( !( -e $s_dir ) ) { die( (caller(0))[3] . " " . $s_dir . " - doesn't exists!\n" ); }
	if( !( -d $s_dir ) ) { die( (caller(0))[3] . " " . $s_dir . " - not a directory!\n" ); } 
	
	my $s_command = "find " . $s_dir . " -type d";
	my @s_res = `$s_command`;
	chomp( @s_res );
	
	return @s_res;
}


#Get list of files, that contains string
sub findFilesThatContainsString
{
	my $s_dir_path = healDirPath( shift() );
	my $s_string = shift();
	if( !( -e $s_dir_path ) ) { die( (caller(0))[3] . " " . $s_dir_path . " - doesn't exists!\n" ); }
	if( !( -d $s_dir_path ) ) { die( (caller(0))[3] . " " . $s_dir_path . " - not a directory!\n" ); }
	
	my $s_command = "find " . $s_dir_path . " -type f -print0 | xargs -0 grep -l \"" . $s_string . "\"";
	my @s_res = `$s_command`;
	
	return @s_res;
}


#Write array to new file
sub writeStringArrToNewFile
{
	my $s_out_file_path = shift();
	my @s_arr = @{ shift() };
	chomp( @s_arr );
	
	system( "cat /dev/null > " . $s_out_file_path );
	if( !( -e $s_out_file_path ) ) { die( (caller(0))[3] . " " . $s_out_file_path . " - doesn't exists!\n" ); }
	
	for( my $i = 0; $i < @s_arr; $i++ ) { $s_arr[ $i ] .= "\n"; }
	
	open( my $file, '>', $s_out_file_path ) or die( "Cannot open " . $s_out_file_path . "!" );
	print $file @s_arr;
	
	close( $file );
}


1;
