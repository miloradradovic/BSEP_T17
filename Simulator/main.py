import random
from datetime import datetime
import time

log_types = ["INFO", "ERROR", "WARN", "TRACE"]
files = ["../hospital/src/main/resources/simulatedlogs/simulator_logs1.log", "../hospital/src/main/resources/simulatedlogs/simulator_logs2.log", "../hospital/src/main/resources/simulatedlogs/simulator_logs3.log", "../hospital/src/main/resources/simulatedlogs/simulator_logs4.log", "../hospital/src/main/resources/simulatedlogs/simulator_logs5.log"]
states = ["no_attack", "error", "brute_force", "dangerous_ip", "dos", "no_login_same_ip"]
dangerous_ips = []

def read_from_file():
    f = open("../hospital/src/main/resources/dangerous_ips.txt", "r")
    for line in f:
        if line.rstrip() != "":
            dangerous_ips.append(line.rstrip())
    f.close()

class LogGenerator:
    def __init__(self, type):
        self.type = type

    def generate_logs(self):

        while True:
            read_from_file()
            print("Starting generating for type " + self.type)
            if self.type == "no_attack":
                self.generate_no_attack()
            elif self.type == "error":
                self.generate_error_state()
            elif self.type == "brute_force":
                self.generate_brute_force()
            elif self.type == "dangerous_ip":
                self.generate_dangerous_ip()
            elif self.type == "no_login_same_ip":
                self.generate_no_login_same_ip()
            else:  # dos
                self.generate_dos()
            print("Finished generating for type " + self.type)
            self.type = generate_next_random_type()
            time.sleep(7)

    def generate_no_attack(self):
        f = open(random.choice(files), "a")
        date_now = datetime.now()
        login = date_now.strftime("%Y-%m-%d %H:%M:%S") + " INFO 10.10.10.10 logged@admin.com Login successful"
        logout = date_now.strftime("%Y-%m-%d %H:%M:%S") + " INFO 10.10.10.10 logged@admin.com Logout successful"
        f.write(login + "\n")
        f.write(logout + "\n")
        f.close()

    def generate_error_state(self):
        f = open(random.choice(files), "a")
        date_now = datetime.now()
        error_log = date_now.strftime("%Y-%m-%d %H:%M:%S") + " ERROR 10.10.10.10 logged@admin.com Something went wrong."
        f.write(error_log + "\n")
        f.close()

    def generate_brute_force(self):
        f = open(random.choice(files), "a")
        date_now = datetime.now()
        for i in range(50):
            brute_force = date_now.strftime("%Y-%m-%d %H:%M:%S") + " WARN 10.10.10.10 logged@admin.com Login " \
                                                                   "unsuccessful "
            f.write(brute_force + "\n")
        f.close()

    def generate_dangerous_ip(self):
        f = open(random.choice(files), "a")
        date_now = datetime.now()
        error_log = date_now.strftime("%Y-%m-%d %H:%M:%S") + " INFO " + random.choice(dangerous_ips) + " logged@admin.com Saving certificate."
        f.write(error_log + "\n")
        f.close()

    def generate_dos(self):
        f = open(random.choice(files), "a")
        date_now = datetime.now()
        for i in range(100):
            dos_log = date_now.strftime("%Y-%m-%d %H:%M:%S") + " INFO 10.10.10.10 logged@admin.com Attempting to save certificate."
            f.write(dos_log + "\n")
        f.close()

    def generate_no_login_same_ip(self):
        f = open(random.choice(files), "a")
        date_now = datetime.now()
        for i in range(31):
            dos_log = date_now.strftime(
                "%Y-%m-%d %H:%M:%S") + " WARN 11.11.11.11 logged@admin.com Login unsuccessful."
            f.write(dos_log + "\n")
        f.close()

def generate_next_random_type():
    random_state = random.choice(states)
    return random_state

if __name__ == '__main__':
    log_generator = LogGenerator("no_attack")
    log_generator.generate_logs()
