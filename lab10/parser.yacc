%{
#include <stdio.h>
#include <stdlib.h>

int yyerror(char *s);

int yydebug = 1;
%}
%error-verbose
%token PROGRAM;
%token MYBEGIN;
%token INT;
%token STRING;
%token DISPLAY;
%token READINT;
%token IF;
%token ELSE;
%token WHILE;
%token RETURN;
%token END;

%token IDENTIFIER;
%token INTCONSTANT;
%token STRINGCONSTANT;
%token CHAR;

%token PLUS;
%token MINUS;
%token TIMES;
%token DIV;
%token MOD;
%token EQ;
%token BIGGER;
%token BIGGEREQ;
%token LESS;
%token LESSEQ;
%token EQQ;
%token NEQ;

%token SEMICOLON;
%token OPEN;
%token CLOSE;
%token BRACKETOPEN;
%token BRACKETCLOSE;
%token COMMA;

%start Start 

%%
Start: PROGRAM BRACKETOPEN MYBEGIN CompoundStatement END BRACKETCLOSE {printf("Program ->  program { begin { CompoundStatement } end } \n"); };

CompoundStatement : Statement SEMICOLON CompoundStatement {printf("CompoundStatement -> Statement ; CompoundStatement\n"); }
		  | Statement SEMICOLON {printf("CompoundStatement -> Statement ;\n");}
		  ;

Statement : DeclarationStatement {printf("Statement -> DeclarationStatement \n");}
	    ;

DeclarationStatement: Type IDENTIFIER {printf("DeclarationStatement -> TYPE  IDENTIFIER\n");}
		      ;

Type : INT {printf("Type -> int\n");}
      ;

%%
yyerror(char *s)
{	
	printf("%s\n",s);
}

extern FILE *yyin;

main(int argc, char **argv)
{
	if(argc>1) yyin =  fopen(argv[1],"r");
	if(!yyparse()) fprintf(stderr, "\tOK\n");
} 

