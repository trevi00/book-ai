class Dog():
    
    #클래스 객체 속성 : 클래스의 모든 메서드 외부에서 정의된 속성
    #규칙에따라 __init__메서드보다 먼저 입력됨
    #Dog의 인스턴스에 관계없이 항상 True인 값을 위한 것
    species = 'mammal'
    
    #init메서드는 객체의 인스턴스가 생성되면 그 즉시 자동으로 호출
    def __init__(self, breed, name):
        self.breed = breed
        self.name = name
    

#x는 Dog의 인스턴스이고 breed는 지정된 breed가 됨
x = Dog('월', 'sammy')
#x.breed는 'lab'을 넣었으니 'lab'이 됨

print(type(x))
print(x.breed)
print(x.name)

#역시나 x.breed는 넣어줬던 'lab'때문에 문자열 타입임
print(type(x.breed))

print(x.species)