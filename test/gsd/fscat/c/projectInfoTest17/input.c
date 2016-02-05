#include<something.h>
#ifdef E1
  #ifdef E2
    #ifdef E3
      char* x = "e3" ;
    #elif E4
      char* x = "e4" ;
    #else
      char* x = "something else (1)" ;
    #endif
  #elif E5
    #ifdef E6
      #ifdef E7
         char* x = "e7" ;
      #endif
    #endif
  #endif
#elif E8
  #ifdef E9
     char* x = "e9" ;
  #endif
  #ifdef E10
    #ifdef E11
      char* x = "e11" ;
    #endif
  #endif
#else
  #ifdef E12
    char* x = "e12" ;
  #endif
#endif
