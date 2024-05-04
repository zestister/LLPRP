%{

#include "parser.hpp"
#include <string>

int readInputForLexer( char *buffer, int maxBytesToRead );

#undef YY_INPUT
#define YY_INPUT(buf,result,maxsize) { result = readInputForLexer(buf,maxsize); if(result<=0) result = YY_NULL; }



%}


%option nounistd
%option never-interactive

LP "("
RP ")"
COMMENT "--".*?

%%

[ \t\r]		                // ignore whitespace
{COMMENT} 	                // ignore comments
"{"                           {return T_Left_Big_Bracket;}
[Pp]                          {return T_P;}
[0-9]                         {return T_Mum;}
[A-Z]                         {return T_Capital;}
"<"                           {return T_Left_Angle_Bracket;}
">"                           {return T_Right_Angle_Bracket;}
"["                           {return T_Left_Square_Bracket;} 
"]"                           {return T_Right_Square_Bracket;} 
"a"                           {return T_A;}  
"u"                           {return T_Mu;} 
"v"                           {return T_V;} 
"("                           {return T_Left_Round_Bracket;}
")"                           {return T_Right_Round_Bracket;}
"&"                           {return T_And;} 
"|"                           {return T_Or;} 
"~"                           {return T_Not;} 
"}"                           {return T_Right_Big_Bracket;}
.

%%

int yywrap() {
	return 1;
}

int globalReadOffset;
char *globalInputText = nullptr;

extern int error_result;

int scanMyThing(const std::string& ss) {
	globalInputText = (char*)calloc(ss.length()+30, 1);
	globalReadOffset = 0;
    error_result = 0;  
	memcpy(globalInputText, ss.c_str(), ss.size());
	YY_FLUSH_BUFFER;
	yyparse();
	free(globalInputText);
	BEGIN (INITIAL); // Reset the parser
    return error_result;
}

int readInputForLexer( char *buffer, int maxBytesToRead ) {
    int numBytesToRead = maxBytesToRead;
    int bytesRemaining = strlen(globalInputText)-globalReadOffset;
    int i;
    if ( numBytesToRead > bytesRemaining ) { numBytesToRead = bytesRemaining; }
    for ( i = 0; i < numBytesToRead; i++ ) {
        buffer[i] = globalInputText[globalReadOffset+i];
    }
    globalReadOffset += numBytesToRead;
    return numBytesToRead;
}