package org.rest.model;

public class UserToBank {

        private Long id;
        private Long userId;
        private Long bankId;

        public UserToBank() {
        }

        public UserToBank(Long id, Long userId, Long bankId) {
            this.id = id;
            this.userId = userId;
            this.bankId = bankId;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

    public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public Long getBankId() {
            return bankId;
        }

        public void setBankId(Long bankId) {
            this.bankId = bankId;
        }

}
