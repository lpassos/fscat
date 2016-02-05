char* x =
#ifdef LEVEL1
  #ifdef LEVEL2_0
     #ifdef LEVEL3_0
       "level1;level2_0,level3_0" ;
     #elif defined(LEVEL3_1)
       "level1;level2_0,level3_1" ;
     #elif defined(LEVEL3_2)
       "level1;level2_0,level3_2" ;
     #endif
 #elif LEVEL2_1
    "level1;level2_1,level3_2" ;
 #elif LEVEL2_2
    "level1;level2_1,level3_2" ;
 #endif
#else
  "level1_else;" ;
#endif
