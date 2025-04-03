
import { LoanPage } from "./LoanPage";


export const LOAN_DATA: LoanPage = {
    content: [
        {
            id: 1,
            game: {
                id: 1,
                title: "Juego 1",
                age: 6,
                category: { id: 1, name: "Categoría 1" },
                author: { id: 1, name: "Autor 1", nationality: "Nacionalidad 1" }
            },
            client: {
                id: 1,
                name: "Cliente 1"
            },
            startDate: new Date('2025-04-01'), 
            endDate: new Date('2025-04-10')
        },

        {
            id: 2,
            game: {
                id: 2,
                title: "Juego 2",
                age: 6,
                category: { id: 1, name: "Categoría 1" },
                author: { id: 1, name: "Autor 2", nationality: "Nacionalidad 2" }
            },
            client: {
                id: 1,
                name: "Cliente 1"
            },
            startDate: new Date('2025-04-03'), 
            endDate: new Date('2025-04-12')
        },

        {
            id: 3,
            game: {
                id: 3,
                title: "Juego 3",
                age: 6,
                category: { id: 1, name: "Categoría 1" },
                author: { id: 1, name: "Autor 3", nationality: "Nacionalidad 3" }
            },
            client: {
                id: 3,
                name: "Cliente 3"
            },
            startDate: new Date('2025-04-04'), 
            endDate: new Date('2025-04-14')
        },
        {
            id: 4,
            game: {
                id: 4,
                title: "Juego 4",
                age: 6,
                category: { id: 1, name: "Categoría 1" },
                author: { id: 1, name: "Autor 3", nationality: "Nacionalidad 3" }
            },
            client: {
                id: 3,
                name: "Cliente 3"
            },
            startDate: new Date('2025-04-04'), 
            endDate: new Date('2025-04-14')
        },
        /*  
        {
            id: 5,
            game: {
                id: 3,
                title: "Juego 3",
                age: 6,
                category: { id: 1, name: "Categoría 1" },
                author: { id: 1, name: "Autor 3", nationality: "Nacionalidad 3" }
            },
            client: {
                id: 1,
                name: "Cliente 3"
            },
            startDate: new Date('2025-04-04'), 
            endDate: new Date('2025-04-14')
        },
       {
            id: 6,
            game: {
                id: 3,
                title: "Juego 3",
                age: 6,
                category: { id: 1, name: "Categoría 1" },
                author: { id: 1, name: "Autor 3", nationality: "Nacionalidad 3" }
            },
            client: {
                id: 1,
                name: "Cliente 3"
            },
            startDate: new Date('2025-04-04'), 
            endDate: new Date('2025-04-14')
        }, */
    ],
    pageable: {
        pageSize: 5,
        pageNumber: 0,
        sort: [{ property: 'id', direction: 'ASC' }],
    },
    totalElements: 6,
};