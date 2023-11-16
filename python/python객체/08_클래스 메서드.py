class Figure1:
    count = 0  # 클래스 변수
 
    # 생성자(initializer)
    def __init__(self, width, height):
        # self.* : 인스턴스변수
        self.width = width
        self.height = height
        # 클래스 변수 접근 예
        Figure1.count += 1
    
    # 메서드
    def calc_area(self):
        return self.width * self.height

    # 클래스 메서드
    #self 못씀
    @classmethod
    def print_count(cls):
        return cls.count

figure1 = Figure1(2, 3)
figure2 = Figure1(4, 5)
print(Figure1.count)
print(Figure1.print_count()) # 2
print(figure1.print_count()) # 2