class Circle():
    
    pi = 3.14
    
    #radius를 입력하지 않으면 자동으로 1로 설정
    def __init__(self, radius=1):
        self.radius = radius
        
    def area(self):
        return self.radius * self.radius * self.pi
    
    def circumfrence(self):
        return 2 * self.pi * self.radius
    
mycircle = Circle(10)
print(mycircle.radius)
#메서드를 실행하려면 괄호가 필요함
print(mycircle.area())
print(mycircle.circumfrence())