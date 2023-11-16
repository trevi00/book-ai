class Figure:
    count = 0
    
    def __init__(self, width, height):
        #인스턴스 변수
        self.width = width
        self.height = height
        #클래스 변수 접근 예
        Figure.count += 1
        
    #정적 메서드에서는 클래스 attribute 접근가능
    @staticmethod
    def print_count():
        #클래스 attribute인 count는 되지만 객체 안의 attribute는 접근 불가능
        print(Figure.count)