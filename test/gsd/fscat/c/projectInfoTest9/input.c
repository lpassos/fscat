#ifdef FOO
void foo() {
  printf("foo is defined") ;
}
#else
inline void foo() {}
#endif
