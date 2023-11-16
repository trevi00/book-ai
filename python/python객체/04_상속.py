class Animal():
    def __init__(self, fur):
        print('Animall Created!')
        self.fur = fur
        
    def report(self):
        print("Animal")
        
    def eat(self):
        print('Eating!')
       
#괄호에 Animal을 넣어서 Animal을 상속 받을 것이다 
class Dog(Animal):
    
    #상속에 의해서 fur를 사용해야'만' 하므로 파라미터 fur 기입
    def __init__(self, fur):
        #상위 클래스(Animal)의 인스턴스를 만들고 Dog Created!를 출력하겠다.
        super().__init__(fur)        
        print("Dog Created!")
       
    #오버라이드(덮어쓰기) : report를 다르게 쓰고 싶으니까 사용
    def report(self):
        print("I am a dog!")
        
d = Dog('Fuzzy')
d.eat()
d.report()
print(d.fur)