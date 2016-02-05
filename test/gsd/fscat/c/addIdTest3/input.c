#ifdef FOO
void foo() {
   printf("foo is defined\n") ;
}
#elif BAR
void bar() {
   printf("bar is defined\n") ;
}
#endif

#if !defined(FOO) && !defined(BAR)
void undef() {
  printf("undef\n");
}
#endif