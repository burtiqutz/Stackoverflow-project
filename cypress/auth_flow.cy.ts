
describe('StackOverflow Clone', () => {

    it('Should register a user, log them in, and show their profile', () => {

        cy.intercept('POST', '**/api/users').as('registerRequest');

        cy.visit('http://localhost:4200/register');

        const uniqueUser = `user_${Date.now()}`;
        cy.get('input[name="username"]').type(uniqueUser);
        cy.get('input[name="email"]').type(`${uniqueUser}@test.com`);
        cy.get('input[name="password"]').type('ParolaMea123');
        cy.get('input[name="confirmPassword"]').type('ParolaMea123');

        cy.get('.btn-register').click();


        cy.wait('@registerRequest').its('response.statusCode').should('be.oneOf', [200, 201]);

        cy.visit('http://localhost:4200/login');

        cy.intercept('POST', '**/api/users/login').as('loginRequest'); // ajustează URL-ul de login dacă e altul

        cy.get('input[name="email"]').type(`${uniqueUser}@test.com`);
        cy.get('input[name="password"]').type('ParolaMea123');

        cy.get('.btn-login').click();

        cy.wait('@loginRequest');

        cy.url().should('include', '/profile/');

        cy.contains(uniqueUser).should('be.visible');

        cy.window().should((win) => {
            expect(win.localStorage.getItem('currentUser')).to.not.be.null;
        });
    });
});