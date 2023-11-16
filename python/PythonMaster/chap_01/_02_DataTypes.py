'''
int : 정수
float : 부동 소숫점
str : 문자열
bool : 부울
'''

some_string = "python python python"
print(some_string[1:5])
print(some_string.count('y'))# y가 몇번 나오는가
print(some_string.find('n')) # n이 처음 나오는 곳


print("I have a {}, I have an {}.".format("pen", "apple"))
print("I have a {1}, I have an {0}.".format("pen", "apple"))

print("I have a %s, I have an %s." % ("pen", "apple"))
'''
%s - string
%c - character
%d - int
%f - float
'''
a = float(3.14)
print("a = %f" % (a))
print(f"a = {a}")