%{

/* Definition section */
#include <stdio.h>
#include <stdlib.h>
#include <string>


extern int yylex();
extern int yylex_destroy();
extern bool scanMyThing(const std::string&);
extern int readInputForLexer( char *buffer, int maxBytesToRead );


int yyerror(char const*);
 
%}


/* Rules types */

/*Token specifications */
%token T_Left_Big_Bracket
%token T_P
%token T_Mum
%token T_Capital
%token T_Left_Angle_Bracket
%token T_Right_Angle_Bracket
%token T_Left_Square_Bracket
%token T_Right_Square_Bracket
%token T_A
%token T_Mu
%token T_V
%token T_Left_Round_Bracket
%token T_Right_Round_Bracket
%token T_And
%token T_Or
%token T_Not
%token T_Right_Big_Bracket


%start stmt
 
%%

stmt:formula
        ;

proposition:T_P T_Mum;
action_s:T_Left_Square_Bracket T_A T_Right_Square_Bracket
action_a:T_Left_Angle_Bracket T_A T_Right_Angle_Bracket



formula:proposition
    |action_s formula
    |action_a formula
    |T_Mu T_Capital formula
    |T_V T_Capital formula
    |T_Left_Round_Bracket formula T_Right_Round_Bracket
    |formula T_And formula
    |formula T_Or formula
    |T_Not formula
    ;

 

%%
//code section
int error_result = 0;

int yyerror(char const*msg) {
    error_result = 1;
    return 0; 
}

 